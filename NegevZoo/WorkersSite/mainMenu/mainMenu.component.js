app.controller('mainMenuCtrl', ['$mdDialog', '$scope', 'zooInfoService',
    function mainMenuController($mdDialog, $scope, zooInfoService) {
        initializeComponent();

        function initializeComponent() {
            $scope.isLoading = true;

            var languagesQuery = zooInfoService.getAllLanguages().then(
                function (data) {
                    app.languages           = data.data;
                    app.defaultLanguage     = app.languages.find(lang => lang.name === 'עברית');

                    $scope.isLoading = false;
                },
                function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('אירעה שגיאה במהלך טעינת הנתונים')
                            .ok('סגור')
                    );

                    $scope.isLoading = false;
                }
            )
        }
}]);