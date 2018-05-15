app.controller('zooEnclosuresListCtrl', ['$q', '$scope', '$mdDialog', 'utilitiesService', 'enclosureService', 'animalService', 'mapViewService', 'fileUpload',
    function enclosureListController($q, $scope, $mdDialog, utilitiesService, enclosureService, animalService, mapViewService, fileUpload) {
        initializeComponent();

        $scope.updateEnclosures();
        
        function initializeComponent() {
            $scope.isLoading                = true;
            $scope.baseURL                  = app.baseURL;

            $scope.updateEnclosures         = function () {
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