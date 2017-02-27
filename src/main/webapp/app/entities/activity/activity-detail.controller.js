(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('ActivityDetailController', ActivityDetailController);

    ActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Activity', 'Association'];

    function ActivityDetailController($scope, $rootScope, $stateParams, previousState, entity, Activity, Association) {
        var vm = this;

        vm.activity = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('amasApp:activityUpdate', function(event, result) {
            vm.activity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
