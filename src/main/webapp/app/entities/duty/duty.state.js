(function() {
    'use strict';

    angular
        .module('amasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('duty', {
            parent: 'entity',
            url: '/duty?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.duty.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/duty/duties.html',
                    controller: 'DutyController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('duty');
                    $translatePartialLoader.addPart('weekday');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('duty-detail', {
            parent: 'duty',
            url: '/duty/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.duty.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/duty/duty-detail.html',
                    controller: 'DutyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('duty');
                    $translatePartialLoader.addPart('weekday');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Duty', function($stateParams, Duty) {
                    return Duty.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'duty',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('duty-detail.edit', {
            parent: 'duty-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/duty/duty-dialog.html',
                    controller: 'DutyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Duty', function(Duty) {
                            return Duty.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('duty.new', {
            parent: 'duty',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/duty/duty-dialog.html',
                    controller: 'DutyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                weekday: null,
                                dutyIntrodution: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('duty', null, { reload: 'duty' });
                }, function() {
                    $state.go('duty');
                });
            }]
        })
        .state('duty.edit', {
            parent: 'duty',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/duty/duty-dialog.html',
                    controller: 'DutyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Duty', function(Duty) {
                            return Duty.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('duty', null, { reload: 'duty' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('duty.delete', {
            parent: 'duty',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/duty/duty-delete-dialog.html',
                    controller: 'DutyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Duty', function(Duty) {
                            return Duty.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('duty', null, { reload: 'duty' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
