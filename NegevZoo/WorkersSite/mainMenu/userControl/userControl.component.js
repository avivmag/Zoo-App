app.controller('userControlCtrl', ['$scope', '$mdDialog', 'usersService',
    function userControlController($scope, $mdDialog, usersService) {
        initializeComponent();

        function initializeComponent() {
            $scope.updateUsers          = function () {
                $scope.isLoading            = true;

                usersQuery = usersService.getAllUsers().then(
                    function (data) {
                        $scope.users        = data.data;
                        $scope.isLoading    = false;

                        addEmptyUser($scope.users);
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

            $scope.addUser              = function (user) {
                $scope.isLoading        = true;

                var successContent      = user.isNew ? 'המשתמש נוסף בהצלחה!' : 'המשתמש עודכן בהצלחה!';
                var failContent         = user.isNew ? 'התרחשה שגיאה בעת שמירת המשתמש' : 'התרחשה שגיאה בעת עדכון המשתמש';

                usersService.updateUser(user).then(
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert()
                                .clickOutsideToClose(true)
                                .textContent(successContent)
                                .ok('סגור')
                        );

                        $scope.isLoading = false;

                        $scope.updateUsers();
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
            };

            $scope.confirmDeleteUser = function (ev, user, users) {
                var confirm = $mdDialog.confirm()
                    .title('האם אתה בטוח שברצונך למחוק את משתמש זה?')
                    .textContent('לאחר המחיקה, לא תוכל להחזירו אלא ליצור אותו מחדש')
                    .targetEvent(ev)
                    .ok('אישור')
                    .cancel('ביטול');
    
                $mdDialog.show(confirm).then(function () {
                   deleteUser(user, users);
                });
            }

            $scope.confirmResetPassword = function (ev, user) {
                if (user.isNew) {
                    resetPassword(ev, user);
    
                    return;
                }
                var confirm = $mdDialog.confirm()
                    .title('האם אתה בטוח שברצונך לאפס למשתמש זה את סיסמתו??')
                    .textContent('לאחר האיפוס, המשתמש לא יוכל להכנס עם סיסמתו הישנה.')
                    .targetEvent(ev)
                    .ok('אישור')
                    .cancel('ביטול');
    
                $mdDialog.show(confirm).then(function () {
                   resetPassword(ev, user);
                });
            }

            $scope.updateUsers();
        }

        function deleteUser(user, users) {
            usersService.deleteUser(user.id).then(
                function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('המשתמש נמחק בהצלחה')
                            .ok('סגור')
                    );

                    users.splice(users.indexOf(user), 1);
                },
                function () {
                    $mdDialog.show(
                        $mdDialog.alert()
                            .clickOutsideToClose(true)
                            .textContent('התרחשה שגיאה בעת מחיקת המשתמש')
                            .ok('סגור')
                    );
                });
        }

        function addEmptyUser(users) {
            users.push({ id: 0, isNew: true });
        }
}])
.directive('zooUserControl', function () {
    return {
        templateUrl: 'mainMenu/userControl/userControl.html'
    };
});