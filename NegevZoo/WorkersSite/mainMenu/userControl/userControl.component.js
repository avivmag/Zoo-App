app.controller('zooUserControlCtrl', ['$scope', '$mdDialog', 'usersService', 'utilitiesService',
    function userControlController($scope, $mdDialog, usersService, utilitiesService) {
        $scope.isLoading            = true;

        initializeComponent();

        $scope.updateUsers();

        function initializeComponent() {
            $scope.updateUsers          = function () {

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

            $scope.addUser              = function (user, users) {
                if (!checkUser(user, users)) {
                    return;
                }

                $scope.isLoading        = true;

                var successContent      = user.isNew ? 'המשתמש נוסף בהצלחה!' : 'המשתמש עודכן בהצלחה!';
                var failContent         = user.isNew ? 'התרחשה שגיאה בעת שמירת המשתמש' : 'התרחשה שגיאה בעת עדכון המשתמש';

                usersService.updateUser(user).then(
                    function () {
                        utilitiesService.utilities.alert(successContent);

                        $scope.isLoading = false;

                        $scope.updateUsers();
                    },
                    function () {
                        utilitiesService.utilities.alert(failContent);

                        $scope.isLoading = false;
                    });
            };

            $scope.confirmDeleteUser    = function (ev, user, users) {
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

            $scope.resetPassword        = function (user) {
                promptObject = {
                    title:          'איפוס סיסמא',
                    content:        'הכנס סיסמא חדשה לאיפוס',
                    placeholder:    'סיסמא חדשה',
                    required:       true,
                    okMsg:          'אפס סיסמא',
                    cancelMsg:      'ביטול'
                };

                utilitiesService.utilities.prompt(promptObject).then(
                    function(newPass) {
                        user.password = newPass;

                        if (!checkUser(user)) {
                            return;
                        }

                        usersService.updatePassword(user.id, user.password).then(
                            () => utilitiesService.utilities.alert("הסיסמא אופסה בהצלחה"),
                            () => utilitiesService.utilities.alert("קרתה שגיאה בעת איפוס הסיסמא"));
                    });
            };

            $scope.updateUsername       = function (user, users) {
                if (!checkUser(user, users)) {
                    return;
                }

                usersService.updateUsername(user.id, user.name).then(
                    () => utilitiesService.utilities.alert("שם המשתמש עודכן בהצלחה"),
                    () => utilitiesService.utilities.alert("קרתה שגיאה בעת עדכון שם המשתמש")); 
            }
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

        function checkUser(user, users) {
            if (!user) {
                return false;
            }

            if (!angular.isDefined(user.name) || user.name === '') {
                utilitiesService.utilities.alert('אנא בחר שם משתמש');

                return false;
            }

            if (users && users.filter(u => u.name === user.name).length > 1) {
                utilitiesService.utilities.alert('שם המשתמש הנבחר כבר בשימוש');

                return false;
            }

            if (!angular.isDefined(user.password) || user.password === '') {
                utilitiesService.utilities.alert('אנא בחר סיסמא');

                return false;
            }

            if (user.password.length < 6) {
                utilitiesService.utilities.alert('הסיסמא חייבת להיות לפחות 6 תווים');

                return false;
            }

            return true;
        }
}])
.directive('zooUserControl', function () {
    return {
        templateUrl: 'mainMenu/userControl/userControl.html'
    };
});