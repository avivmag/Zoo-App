app.factory('mapViewService', ['$mdDialog', function ($mdDialog) {
    MapDialogController.$Inject = ['mapService', '$rootScope'];

    function MapDialogController($scope, $mdDialog, selectedObject, selectedUrlProp, selectedUrlX, selectedUrlY, mapService, $rootScope) {
        $scope.isLoading    = true;
        
        initializeMap();

        function initializeMap() {
            $scope.img              = new Image();
            $scope.selectedObject   = selectedObject;
            $scope.selectedUrlProp  = selectedUrlProp;
            $scope.baseURL          = app.baseURL;
            $scope.mapStyle         = { };

            $scope.img.onload       = function () {
                $scope.ratio                = (Math.max($scope.img.width, $scope.img.height) / 480.0);

                $scope.mapStyle.width       = ($scope.img.width / $scope.ratio) + 'px';
                $scope.mapStyle.height      = ($scope.img.height / $scope.ratio) + 'px';
                $scope.mapStyle.overflow    = 'hidden';

                $scope.isLoading            = false;
                
                getIcons();
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
                $scope.clickPosition        = {
                    width:  event.layerX,
                    height: event.layerY,
                    ratio:  $scope.ratio
                };

                selectedObject.markerStyle  = {
                    position:       'fixed',
                    top:            Math.floor($scope.clickPosition.height - ($scope.selectedObjectHeight / (2 * $scope.ratio))) + 'px',
                    left:           Math.floor($scope.clickPosition.width - ($scope.selectedObjectWidth / (2 * $scope.ratio))) + 'px',
                    height:         Math.floor($scope.selectedObjectHeight / $scope.ratio) + 'px',
                    width:          Math.floor($scope.selectedObjectWidth / $scope.ratio) + 'px',
                    borderStyle:    'ridge',
                    borderColor:    'yellow'
                }

                $scope.selectedObjectDone = true;
            }
            
            $scope.close    = function(clickPosition) {
                // Return the offset when the dialog closes.
                $mdDialog.hide(clickPosition);
            }
        }

        function getIcons() {
            var iconsQuery = mapService.getAllMarkers().then(
                function (data) {
                    
                    let markers = data.data.filter(marker => marker.iconUrl != selectedObject[selectedUrlProp]);
                    
                    var selectedObjectImage = new Image();

                    selectedObjectImage.src = app.baseURL + selectedObject[selectedUrlProp];

                    selectedObjectImage.onload = function () {
                        $scope.selectedObjectHeight    = selectedObjectImage.height;
                        $scope.selectedObjectWidth     = selectedObjectImage.width;

                        var leftOffset  = Math.floor(((selectedObject[selectedUrlX] - (selectedObjectImage.width / 2)) / $scope.ratio));
                        var topOffset   = Math.floor(((selectedObject[selectedUrlY] - (selectedObjectImage.height / 2)) / $scope.ratio));
                        
                        if (leftOffset >= 0 && topOffset >= 0) {
                            $scope.selectedObject.markerStyle = {
                                position:   'fixed',
                                left:       leftOffset + 'px',
                                top:        topOffset + 'px',
                                width:      Math.floor($scope.selectedObjectHeight / $scope.ratio) + 'px',
                                height:     Math.floor($scope.selectedObjectWidth / $scope.ratio) + 'px',
                                borderStyle: 'ridge',
                                borderColor: 'yellow'
                            };

                            $scope.selectedObjectDone = true;
                            $rootScope.$apply();
                        }
                    }

                    if (angular.isArray(markers)) {
                        $scope.markers  = [];
                        
                        markers.map(marker => {
                            var markerImage = new Image();

                            markerImage.src = app.baseURL + marker.iconUrl;

                            markerImage.onload = function () {
                                var markerHeight    = markerImage.height;
                                var markerWidth     = markerImage.width;

                                $scope.markers.push({
                                    src:            app.baseURL + marker.iconUrl,
                                    markerStyle:    {
                                        position:   'fixed',
                                        left:       Math.floor(((marker.longitude - (markerImage.width / 2)) / $scope.ratio)) + 'px',
                                        top:        Math.floor(((marker.latitude - (markerImage.height / 2)) / $scope.ratio)) + 'px',
                                        width:      Math.floor(markerHeight / $scope.ratio) + 'px',
                                        height:     Math.floor(markerWidth / $scope.ratio) + 'px'
                                    }
                                });

                                $rootScope.$apply();
                            }
                        });
                    }
                });

            return iconsQuery;
        }
    }
       
    function showMap(ev, selectedObject, selectedUrlProp, selectedUrlX, selectedUrlY) {
        var mapDialog = $mdDialog.show({
            controller:             MapDialogController,
            templateUrl:            'mainMenu/mapView/map.dialog.html',
            parent:                 angular.element(document.body),
            targetEvent:            ev,
            clickOutsideToClose:    true,
            locals: {
                selectedObject,
                selectedUrlProp,
                selectedUrlX,
                selectedUrlY
            }
        });

        return mapDialog;
    }
    
    var mapViewService      = {
        showMap: showMap
    };

    return mapViewService;
}]);