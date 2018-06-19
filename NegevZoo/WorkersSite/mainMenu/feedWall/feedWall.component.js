app.controller('zooFeedWallCtrl', ['$scope', 'zooInfoService', 'utilitiesService',
    function feedWallController($scope, zooInfoService, utilitiesService) {
        // Initialize the feed wall component.
        initializeComponent();

        // Get all the languages.
        app.getLanguages().then(
            (data) => {
                $scope.languages    = data.data;
                $scope.language     = $scope.languages[0];

                // Update the feeds.
                $scope.updateFeed($scope.language);
            });

        function initializeComponent() {
            $scope.isLoading            = true;

            // Initialize the update feed function.
            $scope.updateFeed           = function (language) {
                $scope.language         = language;

                // Get all the feeds.
                zooInfoService.feedWall.getAllFeeds(language.id).then(
                    function (data) {
                        // Set all existing feed walls to true in case of update.
                        $scope.feedWall     = data.data.map(fw => { fw.isFeedWall = true; return fw; });
                        $scope.isLoading    = false;

                        // Add an empty feed for the user to add.
                        addEmptyFeed($scope.feedWall);
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');

                        $scope.isLoading    = false;
                    });
            }

            // Initialize the confirm delete feed function.
            $scope.confirmDeleteFeed    = function (ev, feed, feedWall) {
                utilitiesService.utilities.confirm({ title: 'מחיקת עדכון', text: 'האם אתה בטוח שברצונך למחוק עדכון זה?' }).then(
                    function () { deleteFeed(feed, feedWall); }
                );
            }

            // Initialize the add feed function.
            $scope.addFeed              = function (feed, feeds) {
                // Check the feed validity.
                if (!checkFeedWall(feed, feeds)) {
                    return;
                }

                $scope.isLoading        = true;

                // Initialize the return statement.
                var successContent      = feed.isNew ? 'האירוע נוסף בהצלחה!' : 'האירוע עודכן בהצלחה!';
                var failContent         = feed.isNew ? 'התרחשה שגיאה בעת שמירת האירוע' : 'התרחשה שגיאה בעת עדכון האירוע';

                zooInfoService.feedWall.updateFeed(feed).then(
                    function () {
                        utilitiesService.utilities.alert(successContent);

                        $scope.isLoading = false;

                        // Refresh the feeds.
                        $scope.updateFeed($scope.language);
                    },
                    function () {
                        utilitiesService.utilities.alert(failContent);

                        $scope.isLoading = false;
                    });
            }
        }

        // Adds an empty feed.
        function addEmptyFeed(feedWall) {
            feedWall.push({ isNew: true, language: $scope.language.id, id: 0, isPushMessage: false, isFeedWall: true });
        }

        // Deletes a feed.
        function deleteFeed(feed, feedWall) {
            zooInfoService.feedWall.deleteFeed(feed.id).then(
                function () {
                    utilitiesService.utilities.alert('העדכון נמחק בהצלחה');

                    // Remove the feed from the feeds array.
                    feedWall.splice(feedWall.indexOf(feed), 1);
                },
                function () {
                    utilitiesService.utilities.alert('התרחשה שגיאה בעת מחיקת העדכון');
                });
        }

        // Checks the feed's validity.
        function checkFeedWall(feed, feeds) {
            // If no feed was given, return.
            if (!feed) {
                return false;
            }

            // If the user did not select either is feed wall nor is push message, return.
            if (!feed.isFeedWall && !feed.isPushMessage) {
                utilitiesService.utilities.alert('אנא בחר האם הודעה זו תישלח כהודעות פוש ו\\או כהודעה על קיר העדכונים');

                return false;
            }

            // If the feed does not have a title, return.
            if (!angular.isDefined(feed.title)) {
                utilitiesService.utilities.alert('אנא הכנס כותרת לעדכון');

                return false;
            }

            // If the feed does not have an info, return.
            if (!angular.isDefined(feed.info)) {
                utilitiesService.utilities.alert('אנא הכנס תוכן לעדכון');

                return false;
            }

            if (feeds.filter(f => f.info === feed.info && f.title === feed.title).length > 1) {
                utilitiesService.utilities.alert('קיים עדכון עם תוכן זהה');

                return false;
            }

            // If a push message option was selected, but no recipients, return.
            if (feed.isPushMessage === true && !angular.isDefined(feed.pushRecipients)) {
                utilitiesService.utilities.alert('אנא בחר נמענים להודעת הפוש');

                return false;
            }

            // Fill all the options.
            feed.isPushMessage  = feed.isPushMessage || false;
            feed.isFeedWall     = feed.isFeedWall || false;

            return true;
        }        
}]);