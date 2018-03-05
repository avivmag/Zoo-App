app.controller('zooEventsCtrl', ['$scope', '$mdDialog',
    function zooSpecialEventsController($scope, $mdDialog) {
        $scope.specialEvents = [
            { title: 'בשבוע הקרוב 50% הנחה על כל השתיה בפארק', date: '28.11.18' },
            { title: 'קייטנת נגב זו יוצאת לדרך!', date: '28.11.18' },
        ];

        addEmptySpecialEvent($scope.specialEvents);

        $scope.confirmDeleteSpecialEvent = function (ev, event, events) {
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

        $scope.addNewSpecialEvent = function (specialEvents) {
            addEmptySpecialEvent(specialEvents);
        }

        function addEmptySpecialEvent(specialEvents) {
            specialEvents.push({ title: 'הקלד שם אירוע', isNew: true });
        }
    }])
.directive('zooEvents', function () {
    return {
        templateUrl: 'mainMenu/specialEvents/specialEvents.html'
    };
});