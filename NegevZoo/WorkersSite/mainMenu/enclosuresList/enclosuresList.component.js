app.controller('zooEnclosuresListCtrl', ['$scope', 'utilitiesService', 'enclosureService',
    function enclosureListController($scope, utilitiesService, enclosureService) {
        // Initialize the enclosure list component.
        initializeComponent();

        // Update the enclosures.
        $scope.updateEnclosures();
        
        function initializeComponent() {
            $scope.isLoading                = true;
            $scope.baseURL                  = app.baseURL;

            // Initialize the update enclosure function.
            $scope.updateEnclosures         = function () {
                // Get the enclosures.
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
        }
}]);