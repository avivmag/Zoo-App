app.controller('zooMapCtrl', ['$q', '$scope', 'fileUpload', 'utilitiesService', 'mapService', 'mapViewService',
    function mapCtrl($q, $scope, fileUpload, utilitiesService, mapService, mapViewService) {

        // Initialize the map component.
        initializeComponent();

        // Update the markers.
        $scope.updateMarkers();

        // Initializes the map component.
        function initializeComponent() {
            $scope.isLoading = true;
            $scope.baseURL   = app.baseURL;

            // Initialize the update markers function.
            $scope.updateMarkers            = function () {
                $scope.newMarker = { isNew: true };
                
                // Get the misc markers.
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

            // Initialize the add marker function.
            $scope.addMarker                = function(marker, isExists) {
                // If a longitude or latitude was not given, return.
                if (marker.longitude === undefined || marker.latitude === undefined) {
                    utilitiesService.utilities.alert('אנא בחר מיקום לאייקון על המפה');

                    return;
                }

                // Upload the marker's icon.
                markerUploadQuery = uploadIcon($scope.markerPic, marker);

                $scope.isLoading        = true;

                // Initialize the return statements.
                var successContent      = marker.isNew ? 'האייקון נוסף בהצלחה!' : 'האייקון עודכן בהצלחה!';
                var failContent         = marker.isNew ? 'התרחשה שגיאה בעת שמירת האייקון' : 'התרחשה שגיאה בעת עדכון האייקון';

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

            // Initialize the add map function.
            $scope.addMap                   = function(map) {
                // If no map was given, return.
                if (!angular.isDefined(map) || map === null) {
                    return;
                }
    
                $scope.isLoading        = true;
    
                // Set the upload url.
                var uploadUrl           = 'map/upload';
    
                // Upload the image.
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

            // Initialize the delete marker function.
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

            // Initialize the open map function.
            $scope.openMap                  = function(ev, marker, save) {
                // Upload the icon (if needed).
                var promises = [uploadIcon($scope.markerPic, marker)];

                $q.all(promises).then(
                    () => {
                        $scope.isLoading = false;
                        
                        // Show the map.
                        mapViewService.showMap(ev, marker, 'iconUrl', 'longitude', 'latitude', marker.iconUrl).then(function(clickPosition) {
                            if (angular.isDefined(clickPosition)) {
                                marker.longitude   = Math.floor((clickPosition.width * clickPosition.ratio) + 42)
                                marker.latitude    = Math.floor((clickPosition.height * clickPosition.ratio) + 42);
                            
                                if (save) {
                                    $scope.markerPic = null;
                                    $scope.addMarker(marker, true);
                                }
                            }
                        });
                    });
            }
        }

        // Uploads an icon.
        function uploadIcon(icon, marker) {
            // If no icon was given, return.
            if (!angular.isDefined(icon) || icon === null) {
                return;
            }

            $scope.isLoading = true;
            
            // Set the upload url.
            var uploadUrl               = 'map/misc/upload';

            // Upload the icon.
            var fileUploadQuery         = fileUpload.uploadFileToUrl(icon, uploadUrl).then(
                (success)   => {
                    marker.iconUrl      = success.data[0];

                    $scope.markerPic    = null;
                },
                ()          => {
                    utilitiesService.utilities.alert('אירעה שגיאה במהלך ההעלאה');

                    $scope.markerPic    = null;
                });

            return fileUploadQuery;
        };
    }])
.directive('zooMap', function () {
    return {
        templateUrl: 'mainMenu/map/map.html'
    };
});