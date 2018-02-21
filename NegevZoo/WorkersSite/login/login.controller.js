app.controller("loginCtrl", ['$scope', '$state', function ($scope, $state) {
    $scope.login = function login(username, password) {

        // TODO:: Build login service.
        /*
        $scope.loginQuery = loginService.login(
            username,
            password,
            function () {
                console.log('success!');
            },
            function () {
                console.log('fail!');
            });
        */

        console.log('loggin in with credentials:', username, password);

        $state.go('home');
    }
}]);