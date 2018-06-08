app.factory('mapViewService', ['$mdDialog', function ($mdDialog) {
    MapDialogController.$Inject = ['mapService', '$rootScope'];

    function MapDialogController($scope, $mdDialog, selectedObject, selectedUrlProp, selectedUrlX, selectedUrlY, markerToRemove, mapService, $rootScope) {
        $scope.isLoading    = true;
        
        // Initialize the map.
        initializeMap();

        // Initializes the map.
        function initializeMap() {
            // Set all the scope's variables.
            $scope.img              = new Image();
            $scope.selectedObject   = selectedObject;
            $scope.selectedUrlProp  = selectedUrlProp;
            $scope.baseURL          = app.baseURL;
            $scope.mapStyle         = { };

            // Whenever the map image finishes loading, get the map's resize ratio, and set its style.
            $scope.img.onload       = function () {
                $scope.ratio                = (Math.max($scope.img.width, $scope.img.height) / 480.0);

                $scope.mapStyle.width       = ($scope.img.width / $scope.ratio) + 'px';
                $scope.mapStyle.height      = ($scope.img.height / $scope.ratio) + 'px';
                $scope.mapStyle.overflow    = 'hidden';

                $scope.isLoading            = false;
                
                // Get the map's icons.
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

                // Set the marker's style.
                selectedObject.markerStyle  = {
                    position:       'fixed',
                    top:            Math.floor($scope.clickPosition.height - ($scope.selectedObjectHeight / (2 * $scope.ratio))) + 'px',
                    left:           Math.floor($scope.clickPosition.width - ($scope.selectedObjectWidth / (2 * $scope.ratio))) + 'px',
                    height:         Math.floor($scope.selectedObjectHeight / $scope.ratio) + 'px',
                    width:          Math.floor($scope.selectedObjectWidth / $scope.ratio) + 'px',
                    borderStyle:    'ridge',
                    borderColor:    'yellow'
                }

                // Set the selected object load to be done.
                $scope.selectedObjectDone = true;
            }
            
            // Initializes the close function.
            $scope.close    = function(clickPosition) {
                // Return the offset when the dialog closes.
                $mdDialog.hide(clickPosition);
            }
        }

        // Get the map's icons.
        function getIcons() {
            var iconsQuery = mapService.getAllMarkers().then(
                function (data) {
                    // Remove the selected marker from the map markers. (So it will not show on the map)
                    let markers = data.data.filter(marker => marker.iconUrl != (markerToRemove ? markerToRemove : ''));
                    
                    var selectedObjectImage = new Image();

                    // Get the image of the selected object.
                    selectedObjectImage.src = app.baseURL + selectedObject[selectedUrlProp];

                    // When the image finishes loading, set it's style on the map.
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
                                width:      Math.floor($scope.selectedObjectWidth / $scope.ratio) + 'px',
                                height:     Math.floor($scope.selectedObjectHeight / $scope.ratio) + 'px',
                                borderStyle: 'ridge',
                                borderColor: 'yellow'
                            };

                            // Set the object to done.
                            $scope.selectedObjectDone = true;
                            
                            // Apply the changes.
                            $rootScope.$apply();
                        }
                    }

                    if (angular.isArray(markers)) {
                        // Initialize the scope's markers array.
                        $scope.markers  = [];
                        
                        markers.map(marker => {
                            var markerImage = new Image();

                            // Get the image of the marker.
                            markerImage.src = app.baseURL + marker.iconUrl;

                            // When the marker finishes loading, set it's style on the map.
                            markerImage.onload = function () {
                                var markerHeight    = markerImage.height;
                                var markerWidth     = markerImage.width;

                                $scope.markers.push({
                                    src:            app.baseURL + marker.iconUrl,
                                    markerStyle:    {
                                        position:   'fixed',
                                        left:       Math.floor(((marker.longitude - (markerImage.width / 2)) / $scope.ratio)) + 'px',
                                        top:        Math.floor(((marker.latitude - (markerImage.height / 2)) / $scope.ratio)) + 'px',
                                        width:      Math.floor(markerWidth / $scope.ratio) + 'px',
                                        height:     Math.floor(markerHeight / $scope.ratio) + 'px'
                                    }
                                });

                                // Apply the changes.
                                $rootScope.$apply();
                            }
                        });
                    }
                });

            return iconsQuery;
        }
    }

    // Shows the map.
    function showMap(ev, selectedObject, selectedUrlProp, selectedUrlX, selectedUrlY, markerToRemove) {
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
                selectedUrlY,
                markerToRemove
            }
        });

        return mapDialog;
    }
    
    // Initialize the service's exported methods.
    var mapViewService      = {
        showMap: showMap
    };

    return mapViewService;
}]);