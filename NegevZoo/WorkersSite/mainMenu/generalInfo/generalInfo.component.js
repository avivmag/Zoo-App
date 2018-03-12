app.controller('generalInfoCtrl', ['$scope', '$mdDialog',
    function generalInfoController($scope, $mdDialog) {
        $scope.feedWall = [
            { title: 'בשבוע הקרוב 50% הנחה על כל השתיה בפארק', date: '28.11.18' },
            { title: 'קייטנת נגב זו יוצאת לדרך!', date: '28.11.18' },
        ];

        addEmptyFeed($scope.feedWall);

        $scope.confirmDeleteFeed = function (ev, feed, feedWall) {
            var confirm = $mdDialog.confirm()
                .title('האם אתה בטוח שברצונך למחוק את תוכן זה?')
                .textContent('לאחר המחיקה, לא תוכל להחזירו אלא ליצור אותו מחדש')
                .targetEvent(ev)
                .ok('אישור')
                .cancel('ביטול');

            $mdDialog.show(confirm).then(function () {
                // TODO:: Remove the feed from the wall.
                feedWall.splice(feedWall.indexOf(feed), 1);
            }, function () {
                console.log('the user had canceled deletion');
            });
        }

        $scope.addFeedToWall = function (feed, feedWall) {
            addEmptyFeed(feedWall);
        }

        $scope.addFeed = function (feed) {
            // TODO:: actually add the feed to the wall.
        }

        function addEmptyFeed(feedWall) {
            feedWall.push({ title: 'הקלד עדכון', isNew: true });
        }
    }])
.directive('zooGeneralInfo', function () {
    return {
        templateUrl: 'mainMenu/generalInfo/generalInfo.html'
    };
});