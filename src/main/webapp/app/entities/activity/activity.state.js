(function() {
    'use strict';

    angular
        .module('amasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('activity', {
            parent: 'entity',
            url: '/activity?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.activity.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity/activities.html',
                    controller: 'ActivityController',
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
                    $translatePartialLoader.addPart('activity');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('activity-detail', {
            parent: 'activity',
            url: '/activity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.activity.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity/activity-detail.html',
                    controller: 'ActivityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activity');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Activity', function($stateParams, Activity) {
                    return Activity.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'activity',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('activity-detail.edit', {
            parent: 'activity-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity/activity-dialog.html',
                    controller: 'ActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Activity', function(Activity) {
                            return Activity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activity.new', {
            parent: 'activity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity/activity-dialog.html',
                    controller: 'ActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                actiName: null,
                                actiDate: null,
                                actiPlace: null,
                                actiContent: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('activity', null, { reload: 'activity' });
                }, function() {
                    $state.go('activity');
                });
            }]
        })
        .state('activity.edit', {
            parent: 'activity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity/activity-dialog.html',
                    controller: 'ActivityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Activity', function(Activity) {
                            return Activity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity', null, { reload: 'activity' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activity.delete', {
            parent: 'activity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity/activity-delete-dialog.html',
                    controller: 'ActivityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Activity', function(Activity) {
                            return Activity.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity', null, { reload: 'activity' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
