(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('RoleDetailController', RoleDetailController);

    RoleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Role', 'Amember'];

    function RoleDetailController($scope, $rootScope, $stateParams, previousState, entity, Role, Amember) {
        var vm = this;

        vm.role = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('amasApp:roleUpdate', function(event, result) {
            vm.role = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
