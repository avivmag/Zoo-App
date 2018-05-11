app.factory('mapViewService', ['$mdDialog', function ($mdDialog) {
    MapDialogController.$Inject = ['mapService', '$rootScope'];

    function MapDialogController($scope, $mdDialog, selectedObject, selectedUrlProp, mapService, $rootScope) {
        $scope.isLoading    = true;
        
        initializeMap();

        function initializeMap() {
            $scope.img          = new Image();

            $scope.mapStyle     = { };

            $scope.img.onload   = function () {
                $scope.ratio                = (Math.max($scope.img.width, $scope.img.height) / 480.0);

                // If a icon property is present, try to load the cursor from the url.
                if (selectedObject && selectedObject[selectedUrlProp]) {
                    var urlExtension            = selectedObject[selectedUrlProp].substring(selectedObject[selectedUrlProp].indexOf('.'));
                    var webServerFilePath       = selectedObject[selectedUrlProp].substring(0, selectedObject[selectedUrlProp].indexOf('.')) + "_webServer" + urlExtension;
                    $scope.mapStyle.cursor      = 'url(' + app.baseURL + webServerFilePath + '), auto';
                }

                $scope.mapStyle.width       = ($scope.img.width / $scope.ratio) + 'px';
                $scope.mapStyle.height      = ($scope.img.height / $scope.ratio) + 'px';
                $scope.mapStyle.overflow    = 'hidden';

                $scope.isLoading = false;

                getIcons()
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

        function getIcons() {
            var iconsQuery = mapService.getAllMarkers().then(
                function (data) {
                    let markers = data.data;
                    if (angular.isArray(markers)) {
                        $scope.markers = [];
                        for (let marker of data.data) {
                            var urlExtension            = marker.iconUrl.substring(marker.iconUrl.indexOf('.'));
                            var webServerFilePath       = marker.iconUrl.substring(0, marker.iconUrl.indexOf('.')) + "_webServer" + urlExtension;

                            $scope.markers.push({
                                src:            app.baseURL + webServerFilePath,
                                markerStyle:    {
                                    position:   'fixed',
                                    left:       Math.floor(((marker.longitude -42) / $scope.ratio)) + 'px',
                                    top:        Math.floor(((marker.latitude - 42) / $scope.ratio)) + 'px'
                                }
                            });
                        }
                    }
                });

            return iconsQuery;
        }
    }
       
    function showMap(ev, selectedObject, selectedUrlProp) {
        var mapDialog = $mdDialog.show({
            controller:             MapDialogController,
            templateUrl:            'mainMenu/mapView/map.dialog.html',
            parent:                 angular.element(document.body),
            targetEvent:            ev,
            clickOutsideToClose:    true,
            locals: {
                selectedObject,
                selectedUrlProp
            }
        });

        return mapDialog;
    }
    
    var mapViewService      = {
        showMap: showMap
    };

    return mapViewService;
}]);