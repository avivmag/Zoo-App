app.controller('zooOpeningHoursCtrl', ['$q', '$scope', '$mdDialog','utilitiesService', 'zooInfoService', 'utilitiesService',
    function zooOpeningHoursController($q, $scope, $mdDialog, utilitiesService, zooInfoService, utilitiesService) {
        $scope.isLoading            = true;

        initializeComponent();

        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                $scope.updateOpeningHours($scope.language);
        });

        function initializeComponent() {
            $scope.hours                = utilitiesService.timeSpan.getHours();
            $scope.minutes              = utilitiesService.timeSpan.getMinutes();
            $scope.days                 = utilitiesService.getDays();

            $scope.updateOpeningHours           = function (language) {
                $scope.language             = language;

                openingHoursQuery = zooInfoService.openingHours.getAllOpeningHours().then(
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

                var openingHourNoteQuery    = zooInfoService.openingHours.getOpeningHourNote(language.id).then(
                    function (data) {
                        $scope.openingHourNote = data.data[0] || { openingHourNote: '' };
                    },
                    function () {
                        utilitiesService.utilities.alert("אירעה שגיאה במהלך טעינת הנתונים");
                    });

                var promises = [openingHourNoteQuery, openingHoursQuery];

                $q.all(promises).then(
                    () => $scope.isLoading    = false,
                    () => $scope.isLoading    = false);
            };

            $scope.addOpeningHour               = function (openingHour, openingHours) {
                if (!checkOpeningHour(openingHour, openingHours)) {
                    return;
                }

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

            $scope.confirmDeleteOpeningHour     = function (ev, openingHour, openingHours) {
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

            $scope.addOpeningHourNote           = function (openingHourNote, languageId) {
                zooInfoService.openingHours.updateOpeningHourNote(openingHourNote, languageId).then(
                    () => utilitiesService.utilities.alert('התוכן נשמר בהצלחה'),
                    () => utilitiesService.utilities.alert('אירעה שגיאה בעת שמירת התוכן'));
            }
        }

        function addEmptyOpeningHour(openingHours) {
            openingHours.push({ isNew: true, language: $scope.language.id, day: 0 });
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

        function checkOpeningHour(openingHour, openingHours) {
            if (!openingHour) {
                return false;;
            }

            if (openingHour.day === undefined || !angular.isNumber(openingHour.day) || openingHour.day < 1 || openingHour.day > 7) {
                utilitiesService.utilities.alert('אנא בחר יום חוקי מרשימת הימים');

                return false;;
            }

            if (openingHours.filter(oh => oh.day === openingHour.day).length > 1) {
                utilitiesService.utilities.alert('ישנה כבר שעת פתיחה קיימת ליום זה');

                return false;
            }

            if (!openingHour.startTime ||
                openingHour.startTime.minute === undefined ||
                !angular.isNumber(openingHour.startTime.minute) ||
                openingHour.startTime.minute < 0 ||
                openingHour.startTime.minute > 60) {
                    utilitiesService.utilities.alert('אנא בחר דקת תאריך תחילה חוקית מרשימת הדקות');

                    return false;
            }

            if (!openingHour.startTime ||
                openingHour.startTime.hour === undefined ||
                !angular.isNumber(openingHour.startTime.hour) ||
                openingHour.startTime.hour < 0 ||
                openingHour.startTime.hour > 24) {
                    utilitiesService.utilities.alert('אנא בחר שעת תאריך תחילה חוקית מרשימת השעות');

                    return false;
            }

            if (!openingHour.endTime ||
                openingHour.endTime.minute === undefined ||
                !angular.isNumber(openingHour.endTime.minute) ||
                openingHour.endTime.minute < 0 ||
                openingHour.endTime.minute > 60) {
                    utilitiesService.utilities.alert('אנא בחר דקת תאריך סיום חוקית מרשימת הדקות');
                    
                    return false;
            }

            if (!openingHour.endTime ||
                openingHour.endTime.hour === undefined ||
                !angular.isNumber(openingHour.endTime.hour) ||
                openingHour.endTime.hour < 0 ||
                openingHour.endTime.hour > 24) {
                    utilitiesService.utilities.alert('אנא בחר שעת תאריך סיום חוקית מרשימת השעות');

                    return false;
            }

            return true;
        }
}])
.directive('zooOpeningHours', function () {
    return {
        templateUrl: 'mainMenu/generalInfo/openingHours.html'
    };
});