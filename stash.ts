/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 3.2.1263 on 2025-11-24 16:19:01.

export interface Attributes extends Serializable {
    id: number;
    name: string;
    datatype: AttributesDatatype;
    createdOn: Date;
    updatedOn: Date;
}

export interface ContainerAttributes extends Serializable {
    attributeId: number;
    containerId: number;
    attributeValue: string;
    createdOn: Date;
    updatedOn: Date;
}

export interface ContainerTypes extends Serializable {
    id: number;
    name: string;
    description: string;
    icon: string;
    createdOn: Date;
    updatedOn: Date;
}

export interface Containers extends Serializable {
    id: number;
    barcode: string;
    description: string;
    containerTypeId: number;
    parentContainerId: number;
    isActive: boolean;
    trialId: number;
    projectId: number;
    createdOn: Date;
    updatedOn: Date;
}

export interface Projects extends Serializable {
    id: number;
    name: string;
    description: string;
    userId: number;
    startDate: Date;
    endDate: Date;
    createdOn: Date;
    updatedOn: Date;
}

export interface TransferLogs extends Serializable {
    transferEventId: number;
    containerId: number;
    sourceId: number;
    targetId: number;
    userId: number;
    createdOn: Date;
    updatedOn: Date;
}

export interface Trials extends Serializable {
    id: number;
    name: string;
    description: string;
    projectId: number;
    rowCount: number;
    columnCount: number;
    shapefile: any;
    userId: number;
    startDate: Date;
    endDate: Date;
    createdOn: Date;
    updatedOn: Date;
}

export interface Users extends Serializable {
    id: number;
    name: string;
    emailAddress: string;
    passwordHash: string;
    icon: any;
    userType: UsersUserType;
    lastLogin: Date;
    createdOn: Date;
    updatedOn: Date;
}

export interface ViewTableContainers extends Serializable {
    containerId: number;
    containerBarcode: string;
    containerDescription: string;
    containerTypeId: number;
    containerTypeName: string;
    containerTypeDescription: string;
    containerTypeIcon: string;
    parentId: number;
    parentBarcode: string;
    parentDescription: string;
    parentContainerTypeId: number;
    parentContainerTypeName: string;
    parentContainerTypeDescription: string;
    parentContainerTypeIcon: string;
    containerIsActive: boolean;
    trialId: number;
    trialName: string;
    trialDescription: string;
    projectId: number;
    projectName: string;
    projectDescription: string;
    containerAttributes: ContainerAttributeValue[];
    subContainerCount: number;
    createdOn: Date;
}

export interface ViewTableTransferEvents extends Serializable {
    eventId: number;
    timestamp: Date;
    sourceId: number;
    sourceBarcode: string;
    sourceTypeId: number;
    targetId: number;
    targetBarcode: string;
    targetTypeId: number;
    userId: number;
    userName: string;
    containerCount: number;
    containerIds: number[];
}

export interface ViewTableTransfers extends Serializable {
    transferEventId: number;
    containerId: number;
    containerBarcode: string;
    containerDescription: string;
    sourceId: number;
    sourceBarcode: string;
    sourceDescription: string;
    targetId: number;
    targetBarcode: string;
    targetDescription: string;
    userId: number;
    userName: string;
    createdOn: Date;
    updatedOn: Date;
}

export interface ViewTableUsers extends Serializable {
    id: number;
    name: string;
    emailAddress: string;
    userType: ViewTableUsersUserType;
    lastLogin: Date;
    createdOn: Date;
    stats: { [index: string]: number };
}

export interface BasicUser {
    id: number;
    name: string;
    emailAddress: string;
    password: string;
    userType: UsersUserType;
}

export interface ContainerAttributeValue {
    attributeId: number;
    attributeName: string;
    attributeValue: string;
}

export interface ContainerImport {
    parentContainerId: number;
    items: ViewTableContainers[];
}

export interface ContainerTransfer {
    sourceId: number;
    targetId: number;
}

export interface ContainerTransferEvent {
    date: Date;
    userId: number;
    sourceId: number;
    sourceBarcode: string;
    sourceType: string;
    targetId: number;
    targetBarcode: string;
    targetType: string;
    containerCount: number;
}

export interface Filter {
    column: string;
    comparator: FilterComparator;
    values: string[];
    canBeChanged: boolean;
    safeColumn: string;
}

export interface FilterGroup {
    filters: Filter[];
    filterGroups: FilterGroup[];
    operator: FilterOperator;
}

export interface LoginDetails {
    username: string;
    password: string;
}

export interface PaginatedRequest {
    orderBy: string;
    ascending: number;
    limit: number;
    page: number;
    prevCount: number;
    filters: FilterGroup[];
}

export interface PaginatedResult<T> extends Serializable {
    data: T;
    count: number;
}

export interface Token {
    token: string;
    imageToken: string;
    userType: string;
    id: number;
    fullName: string;
    lifetime: number;
    createdOn: number;
}

export interface UserPasswordUpdate {
    oldPassword: string;
    newPassword: string;
}

export interface Serializable {
}

export const enum FilterComparator {
    isNull = 'isNull',
    isNotNull = 'isNotNull',
    equals = 'equals',
    contains = 'contains',
    between = 'between',
    greaterThan = 'greaterThan',
    greaterOrEquals = 'greaterOrEquals',
    lessThan = 'lessThan',
    startsWith = 'startsWith',
    endsWith = 'endsWith',
    lessOrEquals = 'lessOrEquals',
    jsonSearch = 'jsonSearch',
    arrayContains = 'arrayContains',
    inSet = 'inSet',
}

export const enum FilterOperator {
    and = 'and',
    or = 'or',
}

export const enum AttributesDatatype {
    numeric = 'numeric',
    text = 'text',
    categorical = 'categorical',
    date = 'date',
}

export const enum UsersUserType {
    admin = 'admin',
    regular = 'regular',
    reference = 'reference',
    inactive = 'inactive',
}

export const enum ViewTableUsersUserType {
    admin = 'admin',
    regular = 'regular',
    reference = 'reference',
    inactive = 'inactive',
}
