(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('DutyDetailController', DutyDetailController);

    DutyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Duty', 'Department', 'Amember'];

    function DutyDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Duty, Department, Amember) {
        var vm = this;

        vm.duty = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('amasApp:dutyUpdate', function(event, result) {
            vm.duty = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
