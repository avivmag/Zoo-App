app.controller('zooAboutUsCtrl', ['$rootScope', '$scope', '$mdDialog', 'zooInfoService', 'utilitiesService',
    function zooAboutUsController($rootScope, $scope, $mdDialog, zooInfoService, utilitiesService) {
        $scope.isLoading            = true;

        // Initialize the component.
        initializeComponent();

        // Get the langauges.
        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                // Update the about us data.
                $scope.updateAboutUs($scope.language);
            });

        // Initializes the component.
        function initializeComponent() {
            // Initialize the update about us function.
            $scope.updateAboutUs        = function (language) {
                $scope.language         = language;

                zooInfoService.aboutInfo.getAboutInfo(language.id).then(
                    function (data) {
                        $scope.aboutInfo        = data.data || { aboutUs: '' };
                        $scope.isLoading        = false;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');

                        $scope.isLoading    = false;
                    });
            }

            // Initializes the add about us function.
            $scope.addAboutUs           = function (aboutUs, languageId) {
                // Initialize the about us object.
                var aboutUsObj          = { aboutUs: aboutUs };

                // Update the about us data.
                zooInfoService.aboutInfo.updateAboutInfo(aboutUsObj, languageId).then(
                    () => utilitiesService.utilities.alert('התוכן נשמר בהצלחה'),
                    () => utilitiesService.utilities.alert('אירעה שגיאה בעת שמירת התוכן'));
            }
        }
}]);