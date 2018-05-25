app.controller('zooEnclosureCtrl', [
    '$q',
    '$state',
    '$scope',
    '$stateParams',
    '$mdDialog',
    'utilitiesService',
    'enclosureService',
    'animalService',
    'mapViewService',
    'fileUpload',
    'mapViewService',

    function enclosureController($q, $state, $scope, $stateParams, $mdDialog, utilitiesService, enclosureService, animalService, mapViewService, fileUpload) {
        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];
                
                initializeComponent();
            });
            

        function initializeComponent() {
            $scope.selectedEnclosure    = $stateParams.enclosure;
            $scope.page                 = 'list';
            $scope.baseURL              = app.baseURL;
            $scope.hours                = utilitiesService.timeSpan.getHours();
            $scope.minutes              = utilitiesService.timeSpan.getMinutes();
            $scope.days                 = utilitiesService.getDays();

            $scope.selectedEnclosure    = $stateParams.enclosure || { };
            $scope.isEdit               = angular.isDefined($stateParams.enclosure) ? true : false;

            if ($scope.isEdit) {
                $scope.isLoading            = true;
                var detailsQuery            = enclosureService.enclosureDetails.getEnclosureDetailsById($scope.selectedEnclosure.id).then(
                    function (data) {
                        $scope.enclosureDetails = data.data;

                        for (let i = 1; i <= 4; i++) {
                            if (!$scope.enclosureDetails.some(ed => ed.language === i)) {
                                $scope.enclosureDetails.push({ encId: $scope.selectedEnclosure.id, language: i });
                            }
                        }

                        $scope.enclosureDetails.sort(function(a, b) { return a.language-b.language; });
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });

                var videosQuery             = enclosureService.enclosures.getEnclosureVideosById($scope.selectedEnclosure.id).then(
                    function (data) {
                        $scope.selectedEnclosure.videos = data.data;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });

                var picturesQuery           = enclosureService.enclosures.getEnclosurePicturesById($scope.selectedEnclosure.id).then(
                    function (data) {
                        $scope.selectedEnclosure.pictures = data.data;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });

                var animalsQuery            = animalService.getAnimalsByEnclosure($scope.selectedEnclosure.id).then(
                    function (animals) {
                        $scope.selectedEnclosure.animals = animals.data;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });

                var animalStoriesQuery      = animalService.getAnimalStoriesByEnclosure($scope.selectedEnclosure.id).then(
                    function (animalStories) {
                        $scope.selectedEnclosure.animalStories = animalStories.data;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                    });    

                var recurringEventsQuery    = enclosureService.enclosureDetails.getRecurringEvents($scope.selectedEnclosure.id, $scope.language.id).then(
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

                var promises = [detailsQuery, videosQuery, picturesQuery, animalsQuery, recurringEventsQuery, animalStoriesQuery];

                $q.all(promises).then(
                    () => $scope.isLoading = false,
                    () => $scope.isLoading = false);
            }
            
            $scope.openMap                  = function(ev, selectedEnclosure) {
                mapViewService.showMap(ev, selectedEnclosure, 'markerIconUrl').then(function(clickPosition) {
                    if (angular.isDefined(clickPosition)) {
                        selectedEnclosure.markerY       = Math.floor((clickPosition.width * clickPosition.ratio) + 42)
                        selectedEnclosure.markerX       = Math.floor((clickPosition.height * clickPosition.ratio) + 42);
                    }
                });
            }
            
            $scope.addEnclosure             = function(enclosure) {
                $scope.isLoading            = true;
                    var successContent      = !$scope.isEdit ? 'המתחם נוסף בהצלחה!' : 'המתחם עודכן בהצלחה!';
                    var failContent         = !$scope.isEdit ? 'התרחשה שגיאה בעת שמירת המתחם' : 'התרחשה שגיאה בעת עדכון המתחם';

                    var pictureUploadQuery  = uploadProfilePicture($scope.profilePic, enclosure);

                    var iconUploadQuery     = uploadIcon($scope.iconPic, enclosure);

                    var uploadPromises      = [pictureUploadQuery, iconUploadQuery];

                    $q.all(uploadPromises).then(
                        () => {
                            if (!checkEnclosure(enclosure)) {
                                $scope.isLoading = false;

                                return;
                            }
                            
                            enclosureService.enclosures.updateEnclosure(enclosure).then(
                                function (response) {
                                    utilitiesService.utilities.alert(successContent);
                                    
                                    $stateParams.enclosure = response.data;
                                    initializeComponent();

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

                        $state.go('mainMenu.enclosures.list')
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
                if (!checkVideoUrl(videoUrl)) {
                    return;
                }

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
                if (!checkRecurringEvent(recurringEvent)) {
                    return false;
                }

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

        function checkEnclosure(enclosure) {
            if (!angular.isDefined(enclosure.name) || enclosure.name == '') {
                utilitiesService.utilities.alert('אנא בחר שם למתחם');

                return false;
            }

            if ((enclosure.markerX !== undefined && enclosure.markerX < 0) ||
                (enclosure.markerY !== undefined && enclosure.markerY < 0)) {
                    utilitiesService.utilities.alert('הנקודות שנבחרו למיקום המתחם אינן חוקיות');

                    return false;
            }

            if (enclosure.markerX !== undefined && enclosure.markerY !== undefined &&
                (enclosure.markerIconUrl === undefined || enclosure.markerIconUrl === null)) {
                    utilitiesService.utilities.alert('אין לבחור מיקום מתחם ללא העלאת אייקון.');

                    return false;
            }

            if ((enclosure.markerX === undefined || enclosure.markerX === null || enclosure.markerY === undefined || enclosure.markerY === null) &&
                (enclosure.markerIconUrl !== undefined && enclosure.markerIconUrl !== null)) {
                    utilitiesService.utilities.alert('אין להעלות אייקון ללא בחירת מיקום.');

                    return false;
            }

            return true;
        }

        function checkVideoUrl(videoUrl) {
            if (!videoUrl) {
                return false;
            }

            if ((videoUrl.indexOf('https://www.youtube.com/') === -1) || (videoUrl.indexOf('watch?v=') === -1)) {
                utilitiesService.utilities.alert('אנא הכנס לינק תקין של יוטיוב.');

                return false;
            }

            return true;
        }

        function checkRecurringEvent(recurringEvent) {
            if (!recurringEvent) {
                return false;
            }

            if (!angular.isDefined(recurringEvent.title) || recurringEvent.title === '') {
                utilitiesService.utilities.alert('אנא בחר כותרת לאירוע החוזר.');

                return false;
            }

            if (!angular.isDefined(recurringEvent.description) || recurringEvent.description === '') {
                utilitiesService.utilities.alert('אנא בחר תיאור לאירוע החוזר.');

                return false;
            }

            if (recurringEvent.day === undefined || recurringEvent.day < 1 || recurringEvent.day > 7) {
                utilitiesService.utilities.alert('אנא בחר יום תקין לאירוע החוזר.');

                return false;
            }

            if (recurringEvent.startTime === undefined || 
                recurringEvent.startTime.hour === undefined ||
                recurringEvent.startTime.minute === undefined) {
                    utilitiesService.utilities.alert('אנא בחר שעת התחלה תקינה.');

                    return false;
            }

            if (recurringEvent.endTime === undefined || 
                recurringEvent.endTime.hour === undefined ||
                recurringEvent.endTime.minute === undefined) {
                    utilitiesService.utilities.alert('אנא בחר שעת סוף תקינה.');

                    return false;
            }

            return true;
        }

        function addEmptyRecurringEvent (recurringEvents) {
            recurringEvents.push({ isNew: true, language: $scope.language.id, id: 0, enclosureId: $scope.selectedEnclosure.id });
        }

        function confirmDeleteRecurringEvent (recurringEvents, recurringEvent) {
            // TODO:: actually confirm this.
        }

        function uploadProfilePicture (picture, enclosure) {
            if (!angular.isDefined(picture) || picture === null) {
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
            if (!angular.isDefined(icon) || icon === null) {
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
}]);