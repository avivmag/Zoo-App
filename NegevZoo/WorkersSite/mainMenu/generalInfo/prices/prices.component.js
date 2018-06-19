app.controller('zooPricesCtrl', ['$scope', '$mdDialog', 'zooInfoService', 'utilitiesService',
    function zooPricesController($scope, $mdDialog, zooInfoService, utilitiesService) {
        $scope.isLoading            = true;

        // Initialize the component.
        initializeComponent();

        // Get all the languages.
        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                // Update the prices.
                $scope.updatePrices($scope.language);
            });

        // Initializes the component.
        function initializeComponent() {
            // Initialize the update prices function.
            $scope.updatePrices         = function (language) {
                $scope.language             = language;

                // Get the prices.
                pricesQuery = zooInfoService.prices.getAllPrices(language.id).then(
                    function (data) {
                        $scope.prices       = data.data;
                        $scope.isLoading    = false;

                        // Add an empty price.
                        addEmptyPrice($scope.prices);
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');

                        $scope.isLoading = false;
                    });
            };

            // Initialize the add price function.
            $scope.addPrice             = function (price) {
                // Check the price's validity.
                if (!checkPrice(price)) {
                    return;
                }

                $scope.isLoading        = true;
                
                // Initialize the return statements.
                var successContent      = price.isNew ? 'המחיר נוסף בהצלחה!' : 'המחיר עודכן בהצלחה!';
                var failContent         = price.isNew ? 'התרחשה שגיאה בעת שמירת המחיר' : 'התרחשה שגיאה בעת עדכון המחיר';

                // Update the prices.
                zooInfoService.prices.updatePrice(price).then(
                    function () {
                        utilitiesService.utilities.alert(successContent);

                        $scope.isLoading = false;

                        // Update the prices.
                        $scope.updatePrices($scope.language);
                    },
                    function () {
                        utilitiesService.utilities.alert(failContent);

                        $scope.isLoading = false;
                    });
            };

            // Initialize the confirm delete price function.
            $scope.confirmDeletePrice   = function (price, prices) {
                utilitiesService.utilities.confirm({ title: 'מחיקת מחירון', text: 'האם אתה בטוח שברצונך למחוק מחירון זה?' }).then(
                    function () {
                        deletePrice(price, prices);
                    });

                return;
            }
        }

        // Adds an empty price.
        function addEmptyPrice(prices) {
            prices.push({ isNew: true, language: $scope.language.id, id: 0 });
        }

        // Deletes a price.
        function deletePrice(price, prices) {
            console.log(price.id);

            zooInfoService.prices.deletePrice(price.id).then(
                function () {
                    utilitiesService.utilities.alert('המחירון נמחק בהצלחה');

                    // Delete the price from the prices array.
                    prices.splice(prices.indexOf(price), 1);
                },
                function () {
                    utilitiesService.utilities.alert('התרחשה שגיאה בעת מחיקת התוכן');
                });
        }

        // Checks the price's validity.
        function checkPrice(price) {
            // If no price was given, return.
            if (!price) {
                return false;
            }

            // If the population was not entered, return.
            if (!angular.isDefined(price.population) || price.population === '') {
                utilitiesService.utilities.alert('אנא בחר אוכלוסייה');

                return false;
            }

            // If the price is invalid, return.
            if (price.pricePop === undefined || !angular.isNumber(price.pricePop) || price.pricePop < 0) {
                utilitiesService.utilities.alert('אנא בחר מחיר חוקי(גדול או שווה ל 0)');

                return false;
            }

            return true;
        }
}])
.directive('zooPrices', function () {
    return {
        templateUrl: 'mainMenu/generalInfo/prices.html'
    };
});