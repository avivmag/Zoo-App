app.controller('zooMainMenuCtrl', ['$scope', '$state',
    function mainMenuController($scope, $state) {
        $scope.reRoute = function(to) {
            $state.go(to);
        }
}]);