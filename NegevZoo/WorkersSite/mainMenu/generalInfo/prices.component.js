app.controller('zooPricesCtrl', ['$scope', '$mdDialog',
    function zooPricesController($scope, $mdDialog) {
        $scope.prices = [
            { price: 50, pop: 'רגיל' },
            { price: 25, pop: 'סטודנט' },
            { price: 25, pop: 'חייל' },
            { price: 20, pop: 'מבוגר' },
        ];

        addEmptyPrice($scope.prices);

        $scope.confirmDeletePrice = function (ev, price, prices) {
            var confirm = $mdDialog.confirm()
                .title('האם אתה בטוח שברצונך למחוק את פריט מחירון זה?')
                .textContent('לאחר המחיקה, לא תוכל להחזירו אלא ליצור אותו מחדש')
                .targetEvent(ev)
                .ok('אישור')
                .cancel('ביטול');

            $mdDialog.show(confirm).then(function () {
                // TODO:: Remove the feed from the wall.
                prices.splice(prices.indexOf(price), 1);
            });
        }

        $scope.addNewPrice = function (prices) {
            addEmptyPrice(prices);
        }

        function addEmptyPrice(prices) {
            prices.push({ pop: 'הכנס קבוצה', isNew: true });
        }
    }])
.directive('zooPrices', function () {
    return {
        templateUrl: 'mainMenu/generalInfo/prices.html'
    };
});