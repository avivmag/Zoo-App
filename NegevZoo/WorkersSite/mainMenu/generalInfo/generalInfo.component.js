app.controller('zooGeneralInfoCtrl', ['$scope', '$state',
    function generalInfoController($scope, $state) {
        $scope.reRoute = function(to) {
            $state.go(to);
        }
}]);