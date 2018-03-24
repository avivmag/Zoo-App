app.controller('zooPricesCtrl', ['$scope', '$mdDialog', 'zooInfoService',
    function zooPricesController($scope, $mdDialog, zooInfoService) {
        initializeComponent();

        function initializeComponent() {
            $scope.languages            = app.languages;
            $scope.language             = $scope.languages[0];

            $scope.updatePrices         = function (language) {
                $scope.language             = language;
                $scope.isLoading            = true;

                pricesQuery = zooInfoService.prices.getAllPrices(language.id).then(
                    function (data) {
                        $scope.prices       = data.data;
                        $scope.isLoading    = false;

                        addEmptyPrice($scope.prices);
                    },
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent('אירעה שגיאה במהלך טעינת הנתונים')
                                .ok('סגור')
                        );

                        $scope.isLoading = false;
                    });
            };

            $scope.addPrice             = function (price) {
                $scope.isLoading        = true;
                var successContent      = price.isNew ? 'המחיר נוסף בהצלחה!' : 'המחיר עודכן בהצלחה!';
                var failContent         = price.isNew ? 'התרחשה שגיאה בעת שמירת המחיר' : 'התרחשה שגיאה בעת עדכון המחיר';

                zooInfoService.prices.updatePrice(price).then(
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent(successContent)
                                .ok('סגור')
                        );

                        $scope.isLoading = false;

                        $scope.updatePrices($scope.language);
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

            $scope.confirmDeletePrice   = function (ev, price, prices) {
                var confirm = $mdDialog.confirm()
                    .title('האם אתה בטוח שברצונך למחוק את פריט מחירון זה?')
                    .textContent('לאחר המחיקה, לא תוכל להחזירו אלא ליצור אותו מחדש')
                    .targetEvent(ev)
                    .ok('אישור')
                    .cancel('ביטול');

                $mdDialog.show(confirm).then(function () {
                    deletePrice(price, prices);
                });
            }

            $scope.updatePrices($scope.language);
        }

        function addEmptyPrice(prices) {
            prices.push({ isNew: true, language: $scope.language.id, id: 0 });
        }

        function deletePrice(price, prices) {
            zooInfoService.prices.deletePrice(price.id).then(
                function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('התוכן נמחק בהצלחה')
                            .ok('סגור')
                    );

                    prices.splice(prices.indexOf(price), 1);
                },
                function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('התרחשה שגיאה בעת מחיקת התוכן')
                            .ok('סגור')
                    );
                });
        }
}])
.directive('zooPrices', function () {
    return {
        templateUrl: 'mainMenu/generalInfo/prices.html'
    };
});