app.controller('zooUserControlCtrl', ['$scope', 'usersService', 'utilitiesService',
    function userControlController($scope, usersService, utilitiesService) {
        $scope.isLoading = true;

        // Initialize the users component.
        initializeComponent();

        // Update the users.
        $scope.updateUsers();

        // Initializes the users component.
        function initializeComponent() {
            // Initialize the update users function.
            $scope.updateUsers          = function () {

                usersQuery = usersService.getAllUsers().then(
                    function (data) {
                        $scope.users        = data.data;
                        $scope.isLoading    = false;

                        // Add an empty user for the user to add.
                        addEmptyUser($scope.users);
                    },
                    function () {
                        utilitiesService.utilities.alert('אירעה שגיאה במהלך טעינת הנתונים');

                        $scope.isLoading = false;
                    });
            };

            // Initialize the add user function.
            $scope.addUser              = function (user, users) {
                // Check the user's validity.
                if (!checkUser(user, users)) {
                    return;
                }

                $scope.isLoading        = true;

                // Initialize the return statement.
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

            // Initialize the confirm delete user function.
            $scope.confirmDeleteUser    = function (user, users) {
                utilitiesService.utilities.confirm({ title: 'מחיקת משתמש', text: 'האם אתה בטוח שברצונך למחוק משתמש זה?' }).then(
                    function () {
                        deleteUser(user, users);
                    });

                return;
            }

            // Initialize the confirm reset password function.
            $scope.confirmResetPassword = function (user) {
                // If the user is new, reset it's password without confirming.
                if (user.isNew) {
                    resetPassword(user);
    
                    return;
                }

                utilitiesService.utilities.confirm({ title: 'איפוס סיסמא', text: 'האם אתה בטוח שברצונך לאפס סיסמא למשתמש זה?' }).then(
                    function () {
                        resetPassword(user);
                    });

                return;
            }

            // Initialize the update username function.
            $scope.updateUsername       = function (user, users) {
                // Check for user validity.
                if (!checkUser(user, users)) {
                    return;
                }

                // Update the username.
                usersService.updateUsername(user.id, user.name).then(
                    () => utilitiesService.utilities.alert("שם המשתמש עודכן בהצלחה"),
                    () => utilitiesService.utilities.alert("קרתה שגיאה בעת עדכון שם המשתמש")); 
            }
        }

        // Resets the user's password.
        function resetPassword(user) {
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

                    // Check the user's validity.
                    if (!checkUser(user)) {
                        return;
                    }

                    // Update the password.
                    usersService.updatePassword(user.id, user.password).then(
                        () => utilitiesService.utilities.alert("הסיסמא אופסה בהצלחה"),
                        () => utilitiesService.utilities.alert("קרתה שגיאה בעת איפוס הסיסמא"));
                });
        };

        // Deletes a user.
        function deleteUser(user, users) {
            usersService.deleteUser(user.id).then(
                function () {
                    utilitiesService.utilities.alert('המשתמש נמחק בהצלחה');

                    // Remove the user from the users array.
                    users.splice(users.indexOf(user), 1);
                },
                function () {
                    utilitiesService.utilities.alert('התרחשה שגיאה בעת מחיקת המשתמש');
                });
        }

        // Adds an empty user.
        function addEmptyUser(users) {
            users.push({ id: 0, isNew: true });
        }

        // Checks the user's validity.
        function checkUser(user, users) {
            // If no user was given, return.
            if (!user) {
                return false;
            }

            // If no username was given, return.
            if (!angular.isDefined(user.name) || user.name === '') {
                utilitiesService.utilities.alert('אנא בחר שם משתמש');

                return false;
            }

            // If the username was already taken, return.
            if (users && users.filter(u => u.name === user.name).length > 1) {
                utilitiesService.utilities.alert('שם המשתמש הנבחר כבר בשימוש');

                return false;
            }

            // If no password was given, return.
            if (!angular.isDefined(user.password) || user.password === '') {
                utilitiesService.utilities.alert('אנא בחר סיסמא');

                return false;
            }

            // If the password's length was under 6 characters, return.
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