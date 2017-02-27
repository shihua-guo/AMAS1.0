(function() {
    'use strict';

    angular
        .module('amasApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('department', {
            parent: 'entity',
            url: '/department?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.department.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/department/departments.html',
                    controller: 'DepartmentController',
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
                    $translatePartialLoader.addPart('department');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('department-detail', {
            parent: 'department',
            url: '/department/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'amasApp.department.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/department/department-detail.html',
                    controller: 'DepartmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('department');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Department', function($stateParams, Department) {
                    return Department.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'department',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('department-detail.edit', {
            parent: 'department-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/department/department-dialog.html',
                    controller: 'DepartmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Department', function(Department) {
                            return Department.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('department.new', {
            parent: 'department',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/department/department-dialog.html',
                    controller: 'DepartmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                deptName: null,
                                deptIntrodution: null,
                                deptImage: null,
                                deptImageContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('department', null, { reload: 'department' });
                }, function() {
                    $state.go('department');
                });
            }]
        })
        .state('department.edit', {
            parent: 'department',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/department/department-dialog.html',
                    controller: 'DepartmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Department', function(Department) {
                            return Department.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('department', null, { reload: 'department' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('department.delete', {
            parent: 'department',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/department/department-delete-dialog.html',
                    controller: 'DepartmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Department', function(Department) {
                            return Department.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('department', null, { reload: 'department' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
