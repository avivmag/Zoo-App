app.controller('zooEventsCtrl', ['$scope', '$mdDialog',
    function zooEventsController($scope, $mdDialog) {
        $scope.events = [
            { title: 'בשבוע הקרוב 50% הנחה על כל השתיה בפארק', date: '28.11.18' },
            { title: 'קייטנת נגב זו יוצאת לדרך!', date: '28.11.18' },
        ];

        addEmptyEvent($scope.events);

        $scope.confirmDeleteEvent = function (ev, event, events) {
            var confirm = $mdDialog.confirm()
                .title('האם אתה בטוח שברצונך למחוק את אירוע זה?')
                .textContent('לאחר המחיקה, לא תוכל להחזירו אלא ליצור אותו מחדש')
                .targetEvent(ev)
                .ok('אישור')
                .cancel('ביטול');

            $mdDialog.show(confirm).then(function () {
                // TODO:: Remove the feed from the wall.
                feedWall.splice(events.indexOf(event), 1);
            }, function () {
                console.log('the user had canceled deletion');
            });
        }

        $scope.addFeed = function (feed) {
            // TODO:: actually add the feed to the wall.
        }

        function addEmptyFeed(feedWall) {
            feedWall.push({ title: 'הקלד שם אירוע', isNew: true });
        }
    }])
.directive('zooEvents', function () {
    return {
        templateUrl: 'mainMenu/feedWall/feedWall.html'
    };
});