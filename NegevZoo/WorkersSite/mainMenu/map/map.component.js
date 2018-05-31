app.controller('zooMapCtrl', ['$q', '$scope', 'fileUpload', 'utilitiesService', 'mapService', 'mapViewService',
    function mapCtrl($q, $scope, fileUpload, utilitiesService, mapService, mapViewService) {

        initializeComponent();

        $scope.updateMarkers();

        function initializeComponent() {
            $scope.isLoading = true;
            $scope.baseURL   = app.baseURL;

            $scope.updateMarkers            = function () {
                $scope.newMarker = { isNew: true };
                
                mapService.getMiscMarkers().then(
                    function (data) {
                        $scope.markers      = data.data;
                        $scope.isLoading    = false;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');
                        
                        $scope.isLoading    = false;
                    });
            };

            $scope.addMarker                = function(marker, isExists) {
                markerUploadQuery       = uploadIcon($scope.markerPic, marker);

                if (!isExists && !angular.isDefined(markerUploadQuery)) {
                    utilitiesService.utilities.alert('אנא בחר תמונה לאייקון השונות');

                    return;
                }

                if (marker.longitude === undefined || marker.latitude === undefined) {
                    utilitiesService.utilities.alert('אנא בחר מיקום לאייקון על המפה');

                    return;
                }

                $scope.isLoading        = true;

                var successContent      = marker.isNew ? 'האייקון נוסף בהצלחה!' : 'האייקון עודכן בהצלחה!';
                var failContent         = marker.isNew === 'create' ? 'התרחשה שגיאה בעת שמירת האייקון' : 'התרחשה שגיאה בעת עדכון האייקון';

                $q.all([markerUploadQuery]).then(
                    () => {
                        mapService.updateMiscMarker(marker).then(
                            function (response) {
                                utilitiesService.utilities.alert(successContent);

                                $scope.updateMarkers();
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

            $scope.addMap                   = function(map) {
                if (!angular.isDefined(map) || map === null) {
                    return;
                }
    
                $scope.isLoading        = true;
    
                var uploadUrl           = 'map/upload';
    
                var fileUploadQuery     = fileUpload.uploadFileToUrl(map, uploadUrl).then(
                    (success)   => {
                        $scope.mapPic               = null;
                        $scope.isLoading            = false;
                        utilitiesService.utilities.alert('המפה עודכנה בהצלחה');
                    },
                    ()          => {
                        $scope.isLoading            = false;
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                    });    
            }

            $scope.deleteMarker             = function(markerId) {
                $scope.isLoading        = true;

                mapService.deleteMiscMarker(markerId).then(
                    function() {
                        utilitiesService.utilities.alert("האייקון נמחק בהצלחה!");
                        
                        $scope.updateMarkers();
                    },

                    function () {
                        utilitiesService.utilities.alert("חלה שגיאה במחיקת האייקון.");

                        $scope.isLoading            = false;
                    });
            }

            $scope.openMap                  = function(ev, marker, save) {
                mapViewService.showMap(ev, marker, 'iconUrl').then(function(clickPosition) {
                    if (angular.isDefined(clickPosition)) {
                        marker.longitude   = Math.floor((clickPosition.width * clickPosition.ratio) + 42)
                        marker.latitude    = Math.floor((clickPosition.height * clickPosition.ratio) + 42);
                    
                        if (save) {
                            $scope.markerPic = null;
                            $scope.addMarker(marker, true);
                        }
                    }
                });
            }
        }

        function uploadIcon(icon, marker) {
            if (!angular.isDefined(icon) || icon === null) {
                return;
            }

            var uploadUrl           = 'map/misc/upload';

            var fileUploadQuery     = fileUpload.uploadFileToUrl(icon, uploadUrl).then(
                (success)   => {
                    marker.iconUrl              = success.data[0];

                    $scope.iconPic              = null;
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');
                });

            return fileUploadQuery;
        };
    }])
.directive('zooMap', function () {
    return {
        templateUrl: 'mainMenu/map/map.html'
    };
});