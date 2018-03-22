app.controller('zooEnclosureCtrl', ['$scope', '$mdDialog', '$mdToast', 'enclosureService',
function enclosureController($scope, $mdDialog, $mdToast, enclosureService) {
    initializeComponent();
    
    function initializeComponent() {
        $scope.updateEnclosures     = function () {
            $scope.isLoading        = true;
            
            enclosureService.enclosures.getAllEnclosures().then(
                function (data) {
                    $scope.enclosures   = data.data;
                    $scope.isLoading    = false;
                    
                    for (let e of $scope.enclosures) {
                        e.pictureUrl = app.baseURL + e.pictureUrl;
                    }
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
            }
            
            $scope.switchPage           = function(page, selectedEnclosure) {
                $scope.page                 = page;
                $scope.selectedEnclosure    = selectedEnclosure;
            }
            
            $scope.goToEnclosure        = function (enclosureId) {
                $state.go('enclosure.details', { enclosurId: enclosureId });
            }
            
            $scope.page             = 'list';

            $scope.updateEnclosures();
        }
    }])
    .directive('zooEnclosures', function () {
        return {
            templateUrl: 'mainMenu/enclosures/enclosures.html'
        };
    });