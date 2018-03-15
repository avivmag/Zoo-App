﻿app.controller('zooEventsCtrl', ['$scope', '$mdDialog', 'zooInfoService',
    function zooSpecialEventsController($scope, $mdDialog, zooInfoService) {
        initializeComponent();

        function initializeComponent() {
            $scope.language                     = app.defaultLanguage;

            $scope.updateSpecialEvents          = function (language) {
                $scope.isLoading                = true;

                zooInfoService.specialEvents.getAllSpecialEvents(language).then(
                    function (data) {
                        $scope.specialEvents    = data.data;
                        $scope.isLoading        = false;

                        addEmptySpecialEvent($scope.specialEvents);
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

            $scope.addEvent                     = function (event) {
                $scope.isLoading        = true;
                var successContent      = event.isNew ? 'האירוע נוסף בהצלחה!' : 'האירוע עודכן בהצלחה!';
                var failContent         = event.isNew ? 'התרחשה שגיאה בעת שמירת האירוע' : 'התרחשה שגיאה בעת עדכון האירוע';

                zooInfoService.specialEvents.updateSpecialEvent(event).then(
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent(successContent)
                                .ok('סגור')
                        );

                        $scope.isLoading = false;

                        $scope.updateSpecialEvents($scope.language);
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
            }

            $scope.confirmDeleteSpecialEvent    = function (ev, event, events) {
                var confirm = $mdDialog.confirm()
                    .title('האם אתה בטוח שברצונך למחוק את אירוע זה?')
                    .textContent('לאחר המחיקה, לא תוכל להחזירו אלא ליצור אותו מחדש')
                    .targetEvent(ev)
                    .ok('אישור')
                    .cancel('ביטול');

                $mdDialog.show(confirm).then(function () {
                    // TODO:: Remove the feed from the wall.
                    events.splice(events.indexOf(event), 1);
                });
            }

            $scope.updateSpecialEvents(app.defaultLanguage);
        }

        function addEmptySpecialEvent(specialEvents) {
            var startDate   = new Date();
            var endDate     = new Date();

            specialEvents.push({ title: 'הקלד שם אירוע', isNew: true, startDate, endDate, id: 0, language: $scope.language });
        }
    }])
.directive('zooEvents', function () {
    return {
        templateUrl: 'mainMenu/specialEvents/specialEvents.html'
    };
});