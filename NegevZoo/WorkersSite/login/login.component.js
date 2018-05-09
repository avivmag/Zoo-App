app.controller('loginCtrl', ['$scope', '$state', 'usersService', 'utilitiesService', 
    function ($scope, $state, usersService, utilitiesService) {
        $scope.login = function (username, password) {

            $scope.loginQuery = usersService.login(username, password).then(
                function (response) {
                    //if (response.data) {
                        $state.go('mainMenu');
                    //}
                    //else {
                    //    utilitiesService.utilities.alert("שם משתמש או סיסמא שגויים");
                    //}
                },
                function () {
                    utilitiesService.utilities.alert("שם משתמש או סיסמא שגויים");
                });
        }
}]);