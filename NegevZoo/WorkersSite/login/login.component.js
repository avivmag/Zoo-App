app.controller('loginCtrl', ['$cookies', '$scope', '$state', 'usersService', 'utilitiesService', 
    function ($cookies, $scope, $state, usersService, utilitiesService) {
        $scope.login = function (username, password) {
            $scope.loginQuery = usersService.login(username, password).then(
                function (response) {
                        $state.go('mainMenu.feedWall');
                },
                function () {
                    utilitiesService.utilities.alert("שם משתמש או סיסמא שגויים");
                });
        }
}]);