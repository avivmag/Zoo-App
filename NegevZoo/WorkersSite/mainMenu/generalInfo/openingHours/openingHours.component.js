app.controller('zooOpeningHoursCtrl', ['$q', '$scope', '$mdDialog','utilitiesService', 'zooInfoService', 'utilitiesService',
    function zooOpeningHoursController($q, $scope, $mdDialog, utilitiesService, zooInfoService, utilitiesService) {
        $scope.isLoading            = true;

        // Initialize the component.
        initializeComponent();

        // Get all languages.
        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                // Update the opening hours.
                $scope.updateOpeningHours($scope.language);
        });

        // Initializes the component.
        function initializeComponent() {
            // Set the scope's variables.
            $scope.hours                = utilitiesService.timeSpan.getHours();
            $scope.minutes              = utilitiesService.timeSpan.getMinutes();
            $scope.days                 = utilitiesService.getDays();

            // Initialize the update opening hours function.
            $scope.updateOpeningHours           = function (language) {
                $scope.language                     = language;

                // Get all opening hours.
                openingHoursQuery = zooInfoService.openingHours.getAllOpeningHours().then(
                    function (data) {
                        $scope.openingHours         = data.data;
                        $scope.isLoading            = false;

                        // Parse the time for every opening hour.
                        for (let oh of $scope.openingHours) {
                            oh.startTime    = utilitiesService.timeSpan.parseTimeSpan(oh.startTime);
                            oh.endTime      = utilitiesService.timeSpan.parseTimeSpan(oh.endTime);
                        }

                        // Add an empty opening hour.
                        addEmptyOpeningHour($scope.openingHours);
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים')

                        $scope.isLoading = false;
                    });

                // Get the opening hours note.
                var openingHourNoteQuery    = zooInfoService.openingHours.getOpeningHourNote(language.id).then(
                    function (data) {
                        $scope.openingHourNote = data.data || { openingHourNote: '' };
                    },
                    function () {
                        utilitiesService.utilities.alert("אירעה שגיאה במהלך טעינת הנתונים");
                    });

                var promises = [openingHourNoteQuery, openingHoursQuery];

                $q.all(promises).then(
                    () => $scope.isLoading    = false,
                    () => $scope.isLoading    = false);
            };

            // Initialize the add opening hour function.
            $scope.addOpeningHour               = function (openingHour, openingHours) {
                // Check the opening hour validity.
                if (!checkOpeningHour(openingHour, openingHours)) {
                    return;
                }

                $scope.isLoading        = true;

                // Initialize the return statements.
                var successContent      = openingHour.isNew ? 'שעת הפתיחה נוספה בהצלחה!' : 'שעת הפתיחה עודכנה בהצלחה!';
                var failContent         = openingHour.isNew ? 'התרחשה שגיאה בעת שמירת שעת הפתיחה' : 'התרחשה שגיאה בעת עדכון שעת הפתיחה';

                // Stringify the opening hour's start and end time.
                openingHour.startTime   = utilitiesService.timeSpan.stringifyTimeSpan(openingHour.startTime);
                openingHour.endTime     = utilitiesService.timeSpan.stringifyTimeSpan(openingHour.endTime);

                // Update the opening hour.
                zooInfoService.openingHours.updateOpeningHour(openingHour).then(
                    function () {
                        utilitiesService.utilities.alert(successContent)

                        $scope.isLoading = false;

                        // Refresh the opening hours.
                        $scope.updateOpeningHours($scope.language);
                    },
                    function () {
                        utilitiesService.utilities.alert(failContent);

                        // Re-parse the opening hour's start and end time.
                        openingHour.startTime    = utilitiesService.timeSpan.parseTimeSpan(openingHour.startTime);
                        openingHour.endTime      = utilitiesService.timeSpan.parseTimeSpan(openingHour.endTime);

                        $scope.isLoading = false;
                    });
            };

            // Initialize the confirm delete opening hour.
            $scope.confirmDeleteOpeningHour     = function (openingHour, openingHours) {
                utilitiesService.utilities.confirm({ title: 'מחיקת שעת פתיחה', text: 'האם אתה בטוח שברצונך למחוק שעת פתיחה זו?' }).then(
                    function () {
                        deleteOpeningHour(openingHour, openingHours);
                    });

                return;
            }

            // Initialize the add opening hour note function.
            $scope.addOpeningHourNote           = function (openingHourNote, languageId) {
                // Initialize the opening hour note object.
                var openingHourNoteObj          = { openingHourNote: openingHourNote };

                zooInfoService.openingHours.updateOpeningHourNote(openingHourNoteObj, languageId).then(
                    () => utilitiesService.utilities.alert('התוכן נשמר בהצלחה'),
                    () => utilitiesService.utilities.alert('אירעה שגיאה בעת שמירת התוכן'));
            }
        }

        // Adds an empty opening hour.
        function addEmptyOpeningHour(openingHours) {
            openingHours.push({ isNew: true, language: $scope.language.id, day: 0 });
        }

        // Deletes an opening hour
        function deleteOpeningHour(openingHour, openingHours) {
            zooInfoService.openingHours.deleteOpeningHour(openingHour.id).then(
                function () {
                    utilitiesService.utilities.alert('שעת הפתיחה נמחקה בהצלחה');

                    // Remove the opening hour from the opening hours array.
                    openingHours.splice(openingHours.indexOf(openingHour), 1);
                },
                function () {
                    utilitiesService.utilities.alert('התרחשה שגיאה בעת מחיקת שעת הפתיחה');
                });
        }

        // Checks the opning hour's validity.
        function checkOpeningHour(openingHour, openingHours) {
            // If no opening hour was given, return.
            if (!openingHour) {
                return false;;
            }

            // If the opening hour's day is invalid, return.
            if (openingHour.day === undefined || !angular.isNumber(openingHour.day) || openingHour.day < 1 || openingHour.day > 7) {
                utilitiesService.utilities.alert('אנא בחר יום חוקי מרשימת הימים');

                return false;;
            }

            // If the opening hour's day is taken, return.
            if (openingHours.filter(oh => oh.day === openingHour.day).length > 1) {
                utilitiesService.utilities.alert('ישנה כבר שעת פתיחה קיימת ליום זה');

                return false;
            }

            // If the opening hour's start time minute is invalid, return.
            if (!openingHour.startTime ||
                openingHour.startTime.minute === undefined ||
                !angular.isNumber(openingHour.startTime.minute) ||
                openingHour.startTime.minute < 0 ||
                openingHour.startTime.minute > 60) {
                    utilitiesService.utilities.alert('אנא בחר דקת תאריך תחילה חוקית מרשימת הדקות');

                    return false;
            }

            // If the opening hour's start time hour is invalid, return.
            if (!openingHour.startTime ||
                openingHour.startTime.hour === undefined ||
                !angular.isNumber(openingHour.startTime.hour) ||
                openingHour.startTime.hour < 0 ||
                openingHour.startTime.hour > 24) {
                    utilitiesService.utilities.alert('אנא בחר שעת תאריך תחילה חוקית מרשימת השעות');

                    return false;
            }

            // If the opening hour's end time minute is invalid, return.
            if (!openingHour.endTime ||
                openingHour.endTime.minute === undefined ||
                !angular.isNumber(openingHour.endTime.minute) ||
                openingHour.endTime.minute < 0 ||
                openingHour.endTime.minute > 60) {
                    utilitiesService.utilities.alert('אנא בחר דקת תאריך סיום חוקית מרשימת הדקות');
                    
                    return false;
            }

            // If the opening hour's end time hour is invalid, return.
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