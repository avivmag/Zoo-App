app.controller('zooFeedWallCtrl', ['$scope', '$mdDialog', '$mdToast', 'zooInfoService', 'utilitiesService',
    function feedWallController($scope, $mdDialog, $mdToast, zooInfoService, utilitiesService) {
        initializeComponent();

        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                $scope.updateFeed($scope.language);
            });

        function initializeComponent() {
            $scope.isLoading            = true;

            $scope.updateFeed           = function (language) {
                $scope.language         = language;

                zooInfoService.feedWall.getAllFeeds(language.id).then(
                    function (data) {
                        $scope.feedWall     = data.data.map(fw => { fw.isFeedWall = true; return fw; });
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

            $scope.addFeed              = function (feed) {
                if (!checkFeedWall(feed)) {
                    return;
                }

                $scope.isLoading        = true;
                var successContent      = feed.isNew ? 'האירוע נוסף בהצלחה!' : 'האירוע עודכן בהצלחה!';
                var failContent         = feed.isNew ? 'התרחשה שגיאה בעת שמירת האירוע' : 'התרחשה שגיאה בעת עדכון האירוע';

                zooInfoService.feedWall.updateFeed(feed).then(
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
            feedWall.push({ isNew: true, language: $scope.language.id, id: 0, isPushMessage: false, isFeedWall: true });
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

        function checkFeedWall(feed) {
            if (!feed.isFeedWall && !feed.isPushMessage) {
                utilitiesService.utilities.alert('אנא בחר האם הודעה זו תישלח כהודעות פוש ו\\או כהודעה על קיר העדכונים');

                return false;
            }

            if (!feed) {
                return false;
            }

            if (!angular.isDefined(feed.title)) {
                utilitiesService.utilities.alert('אנא הכנס כותרת לעדכון');

                return false;
            }

            if (!angular.isDefined(feed.info)) {
                utilitiesService.utilities.alert('אנא הכנס תוכן לעדכון');

                return false;
            }

            if (feed.isPushMessage === true && !angular.isDefined(feed.pushRecipients)) {
                utilitiesService.utilities.alert('אנא בחר נמענים להודעת הפוש');

                return false;
            }

            feed.isPushMessage  = feed.isPushMessage || false;
            feed.isFeedWall     = feed.isFeedWall || false;

            if (!feed.isPushMessage && feed.pushRecipients) {
                delete feed.pushRecipients == '';
            }

            return true;
        }        
}]);