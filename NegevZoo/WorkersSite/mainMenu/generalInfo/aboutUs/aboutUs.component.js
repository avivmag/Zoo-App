app.controller('zooAboutUsCtrl', ['$rootScope', '$scope', '$mdDialog', 'zooInfoService', 'utilitiesService',
    function zooAboutUsController($rootScope, $scope, $mdDialog, zooInfoService, utilitiesService) {
        $scope.isLoading            = true;

        initializeComponent();

        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                $scope.updateAboutUs($scope.language);
            });

        function initializeComponent() {
            $scope.updateAboutUs        = function (language) {
                $scope.language         = language;

                zooInfoService.aboutInfo.getAboutInfo(language.id).then(
                    function (data) {
                        $scope.aboutInfo        = data.data[0] || { aboutUs: '' };
                        $scope.isLoading        = false;
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');

                        $scope.isLoading    = false;
                    });
            }

            $scope.addAboutUs           = function (aboutUs, languageId) {
                var aboutUsObj          = { aboutUs: aboutUs };

                zooInfoService.aboutInfo.updateAboutInfo(aboutUsObj, languageId).then(
                    () => utilitiesService.utilities.alert('התוכן נשמר בהצלחה'),
                    () => utilitiesService.utilities.alert('אירעה שגיאה בעת שמירת התוכן'));
            }
        }
}]);