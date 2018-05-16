app.controller('zooFeedWallCtrl', ['$scope', '$mdDialog', '$mdToast', 'zooInfoService',
    function feedWallController($scope, $mdDialog, $mdToast, zooInfoService) {
        initializeComponent();

        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                $scope.updateFeed($scope.language);
            });

        function initializeComponent() {
            $scope.isLoading            = true;
            $scope.isFeedWall           = true;
            $scope.isPushMessage        = false;

            $scope.updateFeed           = function (language) {
                $scope.language         = language;

                zooInfoService.feedWall.getAllFeeds(language.id).then(
                    function (data) {
                        $scope.feedWall     = data.data;
                        $scope.isLoading    = false;

                        addEmptyFeed($scope.feedWall);
                    },
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent('אירעה שגיאה במהלך טעינת הנתונים')
                                .ok('סגור')
                        );

                        $scope.isLoading    = false;
                    });
            }

            $scope.confirmDeleteFeed    = function (ev, feed, feedWall) {
                var confirm = $mdDialog.confirm()
                    .title('האם אתה בטוח שברצונך למחוק את תוכן זה?')
                    .textContent('לאחר המחיקה, לא תוכל להחזירו אלא ליצור אותו מחדש')
                    .targetEvent(ev)
                    .ok('אישור')
                    .cancel('ביטול');

                $mdDialog.show(confirm).then(function () { deleteFeed(feed, feedWall); });
            }

            $scope.addFeed              = function (feed, isFeedWall, isPushMessage) {
                $scope.isLoading        = true;
                var successContent      = feed.isNew ? 'האירוע נוסף בהצלחה!' : 'האירוע עודכן בהצלחה!';
                var failContent         = feed.isNew ? 'התרחשה שגיאה בעת שמירת האירוע' : 'התרחשה שגיאה בעת עדכון האירוע';

                zooInfoService.feedWall.updateFeed(feed, isFeedWall, isPushMessage).then(
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent(successContent)
                                .ok('סגור')
                        );

                        $scope.isLoading = false;

                        $scope.updateFeed($scope.language);
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
        }

        function addEmptyFeed(feedWall) {
            feedWall.push({ isNew: true, language: $scope.language.id, id: 0 });
        }

        function deleteFeed(feed, feedWall) {
            zooInfoService.feedWall.deleteFeed(feed.id).then(
                function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('התוכן נמחק בהצלחה')
                            .ok('סגור')
                    );

                    feedWall.splice(feedWall.indexOf(feed), 1);
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
}]);