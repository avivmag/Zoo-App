app.controller('zooOpeningHoursCtrl', ['$scope', '$mdDialog', 'zooInfoService', 'utilitiesService',
    function zooOpeningHoursController($scope, $mdDialog, zooInfoService, utilitiesService) {
        initializeComponent();

        function initializeComponent() {
            $scope.languages            = app.languages;
            $scope.language             = $scope.languages[0];

            $scope.hours                = utilitiesService.timeSpan.getHours();
            $scope.minutes              = utilitiesService.timeSpan.getMinutes();
            $scope.days                 = utilitiesService.getDays();

            $scope.updateOpeningHours       = function (language) {
                $scope.language             = language;
                $scope.isLoading            = true;

                openingHoursQuery = zooInfoService.openingHours.getAllOpeningHours(language.id).then(
                    function (data) {
                        $scope.openingHours         = data.data;
                        $scope.isLoading            = false;

                        for (let oh of $scope.openingHours) {
                            oh.startTime    = utilitiesService.timeSpan.parseTimeSpan(oh.startTime);
                            oh.endTime      = utilitiesService.timeSpan.parseTimeSpan(oh.endTime);
                        }

                        addEmptyOpeningHour($scope.openingHours);
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

            $scope.addOpeningHour       = function (openingHour) {
                $scope.isLoading        = true;
                var successContent      = openingHour.isNew ? 'שעת הפתיחה נוספה בהצלחה!' : 'שעת הפתיחה עודכנה בהצלחה!';
                var failContent         = openingHour.isNew ? 'התרחשה שגיאה בעת שמירת שעת הפתיחה' : 'התרחשה שגיאה בעת עדכון שעת הפתיחה';

                openingHour.startTime   = utilitiesService.timeSpan.stringifyTimeSpan(openingHour.startTime);
                openingHour.endTime     = utilitiesService.timeSpan.stringifyTimeSpan(openingHour.endTime);

                zooInfoService.openingHours.updateOpeningHour(openingHour).then(
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent(successContent)
                                .ok('סגור')
                        );

                        $scope.isLoading = false;

                        $scope.updateOpeningHours($scope.language);
                    },
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent(failContent)
                                .ok('סגור')
                        );

                        openingHour.startTime    = utilitiesService.timeSpan.parseTimeSpan(openingHour.startTime);
                        openingHour.endTime      = utilitiesService.timeSpan.parseTimeSpan(openingHour.endTime);

                        $scope.isLoading = false;
                    });
            };

            $scope.confirmDeleteOpeningHour   = function (ev, openingHour, openingHours) {
                var confirm = $mdDialog.confirm()
                    .title('האם אתה בטוח שברצונך למחוק את שעת פתיחה זו?')
                    .textContent('לאחר המחיקה, לא תוכל להחזירה אלא ליצור אותה מחדש')
                    .targetEvent(ev)
                    .ok('אישור')
                    .cancel('ביטול');

                $mdDialog.show(confirm).then(function () {
                    deleteOpeningHour(openingHour, openingHours);
                });
            }

            $scope.updateOpeningHours($scope.language);
        }

        function addEmptyOpeningHour(openingHours) {
            openingHours.push({ isNew: true, language: $scope.language.id, id: 0 });
        }

        function deleteOpeningHour(openingHour, openingHours) {
            zooInfoService.openingHours.deleteOpeningHour(openingHour.id).then(
                function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('התוכן נמחק בהצלחה')
                            .ok('סגור')
                    );

                    openingHours.splice(openingHours.indexOf(openingHour), 1);
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
.directive('zooOpeningHours', function () {
    return {
        templateUrl: 'mainMenu/generalInfo/openingHours.html'
    };
});