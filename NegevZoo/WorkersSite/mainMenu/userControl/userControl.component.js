app.controller('userControlCtrl', ['$scope', '$mdDialog',
    function userControlController($scope, $mdDialog) {
        $scope.users = [
            { username: 'moshe123' },
            { username: 'papo456' },
        ];

        addEmptyUser($scope.users);

        $scope.confirmDeleteUser = function (ev, user, users) {
            var confirm = $mdDialog.confirm()
                .title('האם אתה בטוח שברצונך למחוק את משתמש זה?')
                .textContent('לאחר המחיקה, לא תוכל להחזירו אלא ליצור אותו מחדש')
                .targetEvent(ev)
                .ok('אישור')
                .cancel('ביטול');

            $mdDialog.show(confirm).then(function () {
                // TODO:: Remove the user.
                users.splice(users.indexOf(user), 1);
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

        $scope.addUser = function (user) {
            // TODO:: actually add the user.
        }

        $scope.addNewUser = function (users) {
            addEmptyUser(users);
        }

        function addEmptyUser(users) {
            users.push({ username: 'הקלד שם משתמש', isNew: true });
        }

        function resetPassword(ev, user) {
            var prompt = $mdDialog.prompt()
                .title('הכנס סיסמא חדשה')
                .placeholder('הכנס סיסמא')
                .ariaLabel('Enter label')
                .targetEvent(ev)
                .required(true)
                .ok('שמור')
                .cancel('ביטול');
        
            $mdDialog.show(prompt).then(function(newPass) {
                console.log('pass changed', newPass)
            });
        }

        $scope.addUser = function (users) {
            addEmptyUser(users);
        }
    }])
.directive('zooUserControl', function () {
    return {
        templateUrl: 'mainMenu/userControl/userControl.html'
    };
});