app.controller('zooEnclosureCtrl', ['$q', '$scope', '$mdDialog', 'utilitiesService', 'enclosureService', 'animalService', 'fileUpload',

    function enclosureController($q, $scope, $mdDialog, utilitiesService, enclosureService, animalService, fileUpload) {
        $scope.isLoading            = true;

        initializeComponent();

        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                $scope.updateEnclosures();
            });

        function initializeComponent() {
            $scope.page             = 'list';
            $scope.baseURL          = app.baseURL;
            $scope.hours            = utilitiesService.timeSpan.getHours();
            $scope.minutes          = utilitiesService.timeSpan.getMinutes();
            $scope.days             = utilitiesService.getDays();

            $scope.updateEnclosures         = function () {
                enclosureService.enclosures.getAllEnclosures().then(
                    function (data) {
                        $scope.enclosures   = data.data;
                        $scope.isLoading    = false;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים')
                        
                        $scope.isLoading    = false;
                    });
            };
                
            $scope.switchPage               = function(page, selectedEnclosure) {
                $scope.page                 = page;
                $scope.selectedEnclosure    = selectedEnclosure || { };

                if (page === 'edit') {
                    $scope.isLoading            = true;

                    var detailsQuery            = enclosureService.enclosureDetails.getEnclosureDetailsById(selectedEnclosure.id).then(
                        function (data) {
                            $scope.enclosureDetails = data.data;

                            for (let i = 1; i <= 4; i++) {
                                if (!$scope.enclosureDetails.some(ed => ed.language === i)) {
                                    $scope.enclosureDetails.push({ encId: selectedEnclosure.id, language: i });
                                }
                            }

                            $scope.enclosureDetails.sort(function(a, b) { return a.language-b.language; });
                        },
                        function () {
                            utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                        });

                    var videosQuery             = enclosureService.enclosures.getEnclosureVideosById(selectedEnclosure.id).then(
                        function (data) {
                            $scope.selectedEnclosure.videos = data.data;
                        },
                        function () {
                            utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                        });

                    var picturesQuery           = enclosureService.enclosures.getEnclosurePicturesById(selectedEnclosure.id).then(
                        function (data) {
                            $scope.selectedEnclosure.pictures = data.data;
                        },
                        function () {
                            utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                        });

                    var animalsQuery            = animalService.getAnimalsByEnclosure(selectedEnclosure.id).then(
                        function (animals) {
                            $scope.selectedEnclosure.animals = animals.data;
                        },
                        function () {
                            utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                        });

                    var recurringEventsQuery    = enclosureService.enclosureDetails.getRecurringEvents(selectedEnclosure.id, $scope.language.id).then(
                        function (data) {
                            $scope.selectedEnclosure.recurringEvents = data.data;

                            for (let re of $scope.selectedEnclosure.recurringEvents) {
                                re.startTime    = utilitiesService.timeSpan.parseTimeSpan(re.startTime);
                                re.endTime      = utilitiesService.timeSpan.parseTimeSpan(re.endTime);
                            }

                            addEmptyRecurringEvent($scope.selectedEnclosure.recurringEvents);
                        },
                        function () {
                            utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                        });

                    var promises = [detailsQuery, videosQuery, picturesQuery, animalsQuery, recurringEventsQuery];

                    $q.all(promises).then(
                        () => $scope.isLoading = false,
                        () => $scope.isLoading = false);
                }
            };
                
            $scope.openMap                  = function(ev, selectedEnclosure) {
                $mdDialog.show({
                    controller:             MapDialogController,
                    templateUrl:            'mainMenu/enclosures/map.dialog.html',
                    parent:                 angular.element(document.body),
                    targetEvent:            ev,
                    clickOutsideToClose:    true,
                    locals : {
                        selectedEnclosure:  $scope.selectedEnclosure,
                    }
                })
                .then(function(clickPosition) {
                    if (angular.isDefined(clickPosition)) {
                        selectedEnclosure.markerLongitude   = clickPosition.width * clickPosition.ratio;
                        selectedEnclosure.markerLatitude    = clickPosition.height * clickPosition.ratio;
                    }
                });
            };

            $scope.addEnclosure             = function(enclosure) {
                $scope.isLoading            = true;
                    var successContent      = $scope.page === 'create' ? 'המתחם נוסף בהצלחה!' : 'המתחם עודכן בהצלחה!';
                    var failContent         = $scope.page === 'create' ? 'התרחשה שגיאה בעת שמירת המתחם' : 'התרחשה שגיאה בעת עדכון המתחם';

                    var pictureUploadQuery  = uploadProfilePicture($scope.profilePic, enclosure);

                    var iconUploadQuery     = uploadIcon($scope.iconPic, enclosure);

                    var uploadPromises      = [pictureUploadQuery, iconUploadQuery];

                    $q.all(uploadPromises).then(
                        () => {
                            enclosureService.enclosures.updateEnclosure(enclosure).then(
                                function () {
                                    utilitiesService.utilities.alert(successContent);
                                    
                                    $scope.page     = 'list';
        
                                    $scope.updateEnclosures();

                                },
                                function () {
                                    utilitiesService.utilities.alert(failContent);
        
                                    $scope.isLoading = false;
                                });
                        },
                        () => {
                            utilitiesService.utilities.alert(failContent);

                            $isLoading = false;
                        });
            };

            $scope.deleteEnclosure          = function(encId) {
                $scope.isLoading        = true;

                enclosureService.enclosures.deleteEnclosure(encId).then(
                    function() {
                        utilitiesService.utilities.alert("המתחם נמחק בהצלחה!");

                        $scope.page                 = 'list';
                        $scope.selectedEnclosure    = { };
                        
                        $scope.updateEnclosures();
                    },

                    function () {
                        utilitiesService.utilities.alert("חלה שגיאה במחיקת המתחם.");

                        $scope.isLoading            = false;
                    });
            }

            $scope.addEnclosureDetail       = function(enclosureDetail) {
                $scope.isLoading            = true;
                    var successContent      = $scope.page === 'create' ? 'המתחם נוסף בהצלחה!' : 'המתחם עודכן בהצלחה!';
                    var failContent         = $scope.page === 'create' ? 'התרחשה שגיאה בעת שמירת המתחם' : 'התרחשה שגיאה בעת עדכון המתחם';

                    enclosureService.enclosureDetails.updateEnclosureDetail(enclosureDetail).then(
                        function () {
                            utilitiesService.utilities.alert(successContent);

                            $scope.isLoading = false;
                        },
                        function () {
                            utilitiesService.utilities.alert(failContent);

                            $scope.isLoading = false;
                        });
            }

            $scope.addEnclosureVideo        = function(selectedEnclosure, videoUrl) {
                $scope.isLoading        = true;
                var watchString         = videoUrl.split('watch?v=')[1].split('&')[0];
                
                var enclosureVideo      = { enclosureId: selectedEnclosure.id, videoUrl: watchString };

                enclosureService.enclosures.updateVideoById(enclosureVideo).then(
                    function (updatedVideo) {
                        utilitiesService.utilities.alert('הסרטון הועלה בהצלחה!');

                        selectedEnclosure.videos.push(updatedVideo.data);

                        $scope.isLoading    = false;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה בעת העלאת הסרטון.');

                        $scope.isLoading    = false;
                    }
                )
            };

            $scope.uploadEnclosurePictures  = function(pictures, enclosure) {
                if (!angular.isDefined(pictures)) {
                    return;
                }
    
                $scope.isLoading            = true;
    
                var uploadUrl               = `enclosures/${enclosure.id}/upload/bulk`;
    
                var fileUploadQuery         = fileUpload.uploadFileToUrl(pictures, uploadUrl).then(
                    (success)   => {
                        utilitiesService.utilities.alert("התמונות הועלו בהצלחה.");
                        enclosure.pictures  = enclosure.pictures.concat(success.data);

                        $scope.isLoading    = false;
                    },
                    ()          => {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');

                        $scope.isLoading    = false;
                    });
    
            }

            $scope.deletePicture            = function(selectedEnclosureId, picture, pictures) {
                $scope.isLoading = true;

                enclosureService.enclosures.deleteEnclosurePicture(selectedEnclosureId, picture.id).then(
                    () => {
                        utilitiesService.utilities.alert("התמונה נמחקה בהצלחה.");

                        pictures.splice(pictures.indexOf(picture), 1);
                        $scope.isLoading = false;
                    },
                    () => {
                        utilitiesService.utilities.alert("אירעה שגיאה בעת מחיקת התמונה.");

                        $scope.isLoading = false;
                    });
            }

            $scope.deleteVideo              = function(selectedEnclosureId, video, videos) {
                $scope.isLoading = true;

                enclosureService.enclosures.deleteEnclosureVideo(selectedEnclosureId, video.id).then(
                    () => {
                        utilitiesService.utilities.alert("הסרטון נמחק בהצלחה.");

                        videos.splice(videos.indexOf(video), 1);
                        $scope.isLoading = false;
                    },
                    () => {
                        utilitiesService.utilities.alert("אירעה שגיאה בעת מחיקת הסרטון.");

                        $scope.isLoading = false;
                    }
                )
            }

            $scope.updateRecurringEvents    = function(selectedEnclosure, language) {
                $scope.language             = language;

                enclosureService.enclosureDetails.getRecurringEvents(selectedEnclosure.id, language.id).then(
                    function (data) {
                        $scope.selectedEnclosure.recurringEvents = data.data;

                        for (let re of $scope.selectedEnclosure.recurringEvents) {
                            re.startTime    = utilitiesService.timeSpan.parseTimeSpan(re.startTime);
                            re.endTime      = utilitiesService.timeSpan.parseTimeSpan(re.endTime);
                        }

                        addEmptyRecurringEvent($scope.selectedEnclosure.recurringEvents);
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });
            };

            $scope.addRecurringEvent        = function(recurringEvent) {
                var successContent      = recurringEvent.isNew ? 'האירוע החוזר נוסף בהצלחה!' : 'האירוע החוזר עודכן בהצלחה!';
                var failContent         = recurringEvent.isNew ? 'התרחשה שגיאה בעת שמירת האירוע החוזר' : 'התרחשה שגיאה בעת עדכון האירוע החוזר';

                recurringEvent.startTime   = utilitiesService.timeSpan.stringifyTimeSpan(recurringEvent.startTime);
                recurringEvent.endTime     = utilitiesService.timeSpan.stringifyTimeSpan(recurringEvent.endTime);

                enclosureService.enclosureDetails.updateRecurringEvent(recurringEvent).then(
                    function () {
                        utilitiesService.utilities.alert(successContent);

                        $scope.updateRecurringEvents($scope.selectedEnclosure, $scope.language);

                        recurringEvent.startTime   = utilitiesService.timeSpan.parseTimeSpan(recurringEvent.startTime);
                        recurringEvent.endTime     = utilitiesService.timeSpan.parseTimeSpan(recurringEvent.endTime);
                    },
                    function () {
                        utilitiesService.utilities.alert(failContent);

                        recurringEvent.startTime   = utilitiesService.timeSpan.parseTimeSpan(recurringEvent.startTime);
                        recurringEvent.endTime     = utilitiesService.timeSpan.parseTimeSpan(recurringEvent.endTime);
                    });
            };

            $scope.deleteReucrringEvent     = function(selectedEnclosureId, recurringEvents, recurringEvent) {
                enclosureService.enclosureDetails.deleteRecurringEvent(selectedEnclosureId, recurringEvent.id).then(
                    () => {
                        utilitiesService.utilities.alert("האירוע החוזר נמחק בהצלחה.");

                        recurringEvents.splice(recurringEvents.indexOf(recurringEvent), 1);
                    },
                    () => {
                        utilitiesService.utilities.alert("אירעה שגיאה בעת מחיקת האירוע החוזר.");
                    });
            }
        };

        function addEmptyRecurringEvent (recurringEvents) {
            recurringEvents.push({ isNew: true, language: $scope.language.id, id: 0, enclosureId: $scope.selectedEnclosure.id });
        }

        function confirmDeleteRecurringEvent (recurringEvents, recurringEvent) {

        }

        function uploadProfilePicture (picture, enclosure) {
            if (!angular.isDefined(picture)) {
                return;
            }

            $scope.isLoading        = true;

            var uploadUrl           = 'enclosures/upload/pictures';

            var fileUploadQuery     = fileUpload.uploadFileToUrl(picture, uploadUrl).then(
                (success)   => {
                    enclosure.pictureUrl    = success.data[0];

                    $scope.profilePic       = null;
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        };

        function uploadIcon (icon, enclosure) {
            if (!angular.isDefined(icon)) {
                return;
            }

            $scope.isLoading        = true;

            var uploadUrl           = 'enclosures/upload/markers';

            var fileUploadQuery     = fileUpload.uploadFileToUrl(icon, uploadUrl).then(
                (success)   => {
                    enclosure.markerIconUrl     = success.data[0];

                    $scope.iconPic              = null;
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        };

        MapDialogController.$Inject = ['mapService', '$rootScope'];

        function MapDialogController($scope, $mdDialog, selectedEnclosure, mapService, $rootScope) {
            $scope.isLoading    = true;
            
            initializeMap();

            function initializeMap() {
                $scope.img          = new Image();

                $scope.mapStyle     = { };

                $scope.img.onload   = function () {
                    $scope.ratio                = (Math.max($scope.img.width, $scope.img.height) / 640.0);

                    $scope.mapStyle.width       = ($scope.img.width / $scope.ratio) + 'px';
                    $scope.mapStyle.height      = ($scope.img.height / $scope.ratio) + 'px';
                    $scope.mapStyle.cursor      = 'url(' + app.baseURL + selectedEnclosure.markerIconUrl + '), auto';
                    $scope.mapStyle.overflow    = 'hidden';

                    $scope.isLoading = false;

                    $rootScope.$apply();
                };

                // Get the map url from the server.
                mapQuery = mapService.getMap().then(
                    function (data) {
                        $scope.img.src = app.baseURL + data.data[0].url;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה בעת שליפת המפה');
                    });

                $scope.clickMap = function(event) {
                    // Return the click position with the adjustment ratio.
                    var clickPosition   = {
                        width:  event.layerX,
                        height: event.layerY,
                        ratio:  $scope.ratio
                    };

                    // Return the offset when the dialog closes.
                    $mdDialog.hide(clickPosition);
                }
            }
        }
}])
.directive('zooEnclosures', function () {
    return {
        templateUrl: 'mainMenu/enclosures/enclosures.html'
    };
});