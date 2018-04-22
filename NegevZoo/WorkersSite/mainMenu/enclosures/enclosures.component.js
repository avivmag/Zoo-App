app.controller('zooEnclosureCtrl', ['$q', '$scope', '$rootScope', '$mdDialog', 'utilitiesService', 'enclosureService', 'animalService', 'fileUpload',

    function enclosureController($q, $scope, $rootScope, $mdDialog, utilitiesService, enclosureService, animalService, fileUpload) {
        initializeComponent();
        
        $scope.updateEnclosures();

        function initializeComponent() {
            $scope.page             = 'list';
            $scope.baseURL          = app.baseURL;

            $scope.updateEnclosures         = function () {
                $scope.isLoading        = true;

                $scope.languages        = app.languages;
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
                    enclosureService.enclosureDetails.getEnclosureDetailsById(selectedEnclosure.id).then(
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

                    enclosureService.enclosures.getEnclosureVideosById(selectedEnclosure.id).then(
                        function (data) {
                            $scope.selectedEnclosure.videos = data.data;
                        },
                        function () {
                            utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                        });

                    enclosureService.enclosures.getEnclosurePicturesById(selectedEnclosure.id).then(
                        function (data) {
                            $scope.selectedEnclosure.pictures = data.data;
                        },
                        function () {
                            utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                        });

                    animalService.getAnimalsByEnclosure(selectedEnclosure.id).then(
                        function (animals) {
                            $scope.selectedEnclosure.animals = animals.data;
                        },
                        function () {
                            utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                        });
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
                        selectedEnclosure.markerLongitude   = clickPosition.width;
                        selectedEnclosure.markerLatitude    = clickPosition.height;
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

                        selectedEnclosure.videos.push(updatedVideo);

                        $scope.isLoading    = false;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה בעת העלאת הסרטון.');

                        $scope.isLoading    = false;
                    }
                )
            };

            $scope.uploadEnclosurePictures  = function (pictures, enclosure) {
                if (!angular.isDefined(pictures)) {
                    return;
                }
    
                $scope.isLoading            = true;
    
                var uploadUrl               = `enclosures/${enclosure.id}/upload/bulk`;
    
                var fileUploadQuery         = fileUpload.uploadFileToUrl(pictures, uploadUrl).then(
                    (success)   => {
                        utilitiesService.alert("התמונות הועלו בהצלחה.");
                        enclosure.pictures  = enclosure.pictures.concat(success.data);

                        $scope.isLoading    = false;
                    },
                    ()          => {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');

                        $scope.isLoading    = false;
                    });
    
            }

            $scope.deletePicture            = function (selectedEnclosureId, picture, pictures) {
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
                    }
                )
            }

            $scope.deleteVideo              = function (selectedEnclosureId, video, videos) {
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
        };

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

        MapDialogController.$Inject = ['mapService'];

        function MapDialogController($scope, $rootScope, $mdDialog, selectedEnclosure, mapService) {
            $scope.img          = new Image();

            $scope.mapStyle     = { };

            $scope.img.onload   = function () {
                $scope.originalPicWidth = document.getElementById('pic').width;
                
                $scope.mapStyle.width   = $scope.img.width + 'px';
                $scope.mapStyle.height  = $scope.img.height + 'px';
                $scope.mapStyle.cursor  = 'url(' + app.baseURL + selectedEnclosure.markerIconUrl + '), auto';
                
                $rootScope.$apply();
            };
            
            // Get the map url from the server.
            mapQuery = mapService.getMap().then(function (data) {

                $scope.img.src = app.baseURL + data.data[0].url;
            },
            function () {
                utilitiesService.utilities.alert('אירעה שגיאה בעת שליפת המפה');
            });

            $scope.clickMap = function(event) {
                // Get the offset of the adjusted image.
                var widthOffset     = $scope.img.width - $scope.originalPicWidth;

                // Adjust the position by the offset.
                var clickPosition   = {
                    width:  event.layerX + widthOffset,
                    height: event.layerY
                };

                // Return the offset when the dialog closes.
                $mdDialog.hide(clickPosition);
            }
        }
}])
.directive('zooEnclosures', function () {
    return {
        templateUrl: 'mainMenu/enclosures/enclosures.html'
    };
});