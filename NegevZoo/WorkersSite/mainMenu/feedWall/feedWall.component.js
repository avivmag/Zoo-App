app.controller('zooFeedWallCtrl', ['$scope', '$mdDialog',
    function feedWallController($scope, $mdDialog) {
        $scope.feedWall = [
            { title: 'בשבוע הקרוב 50% הנחה על כל השתיה בפארק', date: new Date('2018-11-28') },
            { title: 'קייטנת נגב זו יוצאת לדרך!', date: new Date('2018-11-28') },
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
.directive('zooFeedWall', function () {
    return {
        templateUrl: 'mainMenu/feedWall/feedWall.html'
    };
});