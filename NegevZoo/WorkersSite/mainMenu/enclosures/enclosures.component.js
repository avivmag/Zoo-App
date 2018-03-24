app.controller('zooEnclosureCtrl', ['$scope', '$mdDialog', 'utilitiesService', 'enclosureService', 'fileUpload',

    function enclosureController($scope, $mdDialog, utilitiesService, enclosureService, fileUpload) {
        $scope.page             = 'list';
        $scope.baseURL          = app.baseURL;
        
        initializeComponent();
        
        function initializeComponent() {
            $scope.updateEnclosures     = function () {
                $scope.isLoading        = true;

                $scope.languages        = app.languages;
                enclosureService.enclosures.getAllEnclosures().then(
                    function (data) {
                        $scope.enclosures   = data.data;
                        $scope.isLoading    = false;
                    },
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('אירעה שגיאה במהלך טעינת הנתונים')
                            .ok('סגור')
                        );
                        
                        $scope.isLoading    = false;
                    });
            };
                
            $scope.switchPage           = function(page, selectedEnclosure) {
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
                            $mdDialog.show(
                                $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent('אירעה שגיאה במהלך טעינת הנתונים')
                                .ok('סגור')
                            );
                        });
                }
            };
                
            $scope.openMap              = function(ev, selectedEnclosure) {
                $mdDialog.show({
                    controller:             MapDialogController,
                    templateUrl:            'mainMenu/enclosures/map.dialog.html',
                    parent:                 angular.element(document.body),
                    targetEvent:            ev,
                    clickOutsideToClose:    true,
                })
                .then(function(clickPosition) {
                    selectedEnclosure.markerLongitude   = clickPosition.width;
                    selectedEnclosure.markerLatitude    = clickPosition.height;
                });
            };

            $scope.addEnclosure         = function(enclosure) {
                $scope.isLoading            = true;
                    var successContent      = $scope.page === 'create' ? 'המתחם נוסף בהצלחה!' : 'המתחם עודכן בהצלחה!';
                    var failContent         = $scope.page === 'create' ? 'התרחשה שגיאה בעת שמירת המתחם' : 'התרחשה שגיאה בעת עדכון המתחם';

                    enclosureService.enclosures.updateEnclosure(enclosure).then(
                        function () {
                            $mdDialog.show(
                                $mdDialog.alert()
                                    .clickOutsideToClose(true)
                                    .textContent(successContent)
                                    .ok('סגור')
                            );

                            $scope.isLoading = false;
                        },
                        function () {
                            $mdDialog.show(
                                $mdDialog.alert()
                                    .clickOutsideToClose(true)
                                    .textContent(failContent)
                                    .ok('סגור')
                            );

                            $scope.isLoading = false;
                        });
            };
                
            $scope.addEnclosureDetail   = function(enclosureDetail) {
                $scope.isLoading            = true;
                    var successContent      = $scope.page === 'create' ? 'המתחם נוסף בהצלחה!' : 'המתחם עודכן בהצלחה!';
                    var failContent         = $scope.page === 'create' ? 'התרחשה שגיאה בעת שמירת המתחם' : 'התרחשה שגיאה בעת עדכון המתחם';

                    enclosureService.enclosureDetails.updateEnclosureDetail(enclosureDetail).then(
                        function () {
                            $mdDialog.show(
                                $mdDialog.alert()
                                    .clickOutsideToClose(true)
                                    .textContent(successContent)
                                    .ok('סגור')
                            );

                            $scope.isLoading = false;
                        },
                        function () {
                            $mdDialog.show(
                                $mdDialog.alert()
                                    .clickOutsideToClose(true)
                                    .textContent(failContent)
                                    .ok('סגור')
                            );

                            $scope.isLoading = false;
                        });
            }

            $scope.updateEnclosures();
        };

        $scope.uploadFile = function() {
            var file = $scope.myFile;
                
            console.log(file);
            $scope.isLoading    = true;

            var uploadUrl       = "enclosures/upload";

            fileUpload.uploadFileToUrl(file, uploadUrl).then(function (success) {
                $scope.selectedEnclosure.markerIconUrl = success.data[0];

                utilitiesService.utilities.alert('ההעלאה הושלמה בהצלחה!')

                $scope.isLoading    = false;
            },
            function () {
                $mdDialog.show(
                    $mdDialog.alert()
                    .clickOutsideToClose(true)
                    .textContent('אירעה שגיאה במהלך ההעלאה')
                    .ok('סגור')
                );

                $scope.isLoading    = false;
            });
        }

        function MapDialogController($scope, $mdDialog) {
            $scope.img          = new Image();

            $scope.img.onload = function () {
                $scope.originalPicWidth = document.getElementById('pic').width;

                $scope.mapStyle = {
                    width:  $scope.img.width + 'px',
                    height: $scope.img.height + 'px'
                }
            }
            
            // TODO:: get map from database.
            $scope.img.src = "http://localhost:5987/assets/zoo_map.png";

            $scope.clickMap = function(event) {
                var widthOffset     = $scope.img.width - $scope.originalPicWidth;

                var clickPosition   = {
                    width: event.layerX + widthOffset,
                    height: event.layerY
                };

                $mdDialog.hide(clickPosition);
            }
        }
}])
.directive('zooEnclosures', function () {
    return {
        templateUrl: 'mainMenu/enclosures/enclosures.html'
    };
});