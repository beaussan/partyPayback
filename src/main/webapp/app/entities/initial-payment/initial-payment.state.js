(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('initial-payment', {
            parent: 'entity',
            url: '/initial-payment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'partyPaybackApp.initialPayment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/initial-payment/initial-payments.html',
                    controller: 'InitialPaymentController',
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
                    $translatePartialLoader.addPart('initialPayment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('initial-payment-detail', {
            parent: 'entity',
            url: '/initial-payment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'partyPaybackApp.initialPayment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/initial-payment/initial-payment-detail.html',
                    controller: 'InitialPaymentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('initialPayment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InitialPayment', function($stateParams, InitialPayment) {
                    return InitialPayment.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('initial-payment.new', {
            parent: 'initial-payment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/initial-payment/initial-payment-dialog.html',
                    controller: 'InitialPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ammount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('initial-payment', null, { reload: true });
                }, function() {
                    $state.go('initial-payment');
                });
            }]
        })
        .state('initial-payment.edit', {
            parent: 'initial-payment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/initial-payment/initial-payment-dialog.html',
                    controller: 'InitialPaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InitialPayment', function(InitialPayment) {
                            return InitialPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('initial-payment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('initial-payment.delete', {
            parent: 'initial-payment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/initial-payment/initial-payment-delete-dialog.html',
                    controller: 'InitialPaymentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InitialPayment', function(InitialPayment) {
                            return InitialPayment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('initial-payment', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
