type FrontendTreeNode = {
  name: string
  children?: FrontendTreeNode[]
};

type FrontendViewState = {
  openedFolders: FrontendTreeNode
  searchQuery: string
};

type FrontendMessage = FrontendViewState;

enum DiffType { Same, Added, Deleted, Modified }

enum ItemKind { Folder, File, LeftOnlyFile, RightOnlyFile }

type TreeNodeDiffModel = {
  name: string
  kind: ItemKind
  diffType: DiffType
  // null if children were not loaded yet or both left and right nodes are files
  children?: TreeNodeDiffModel[]
};

type BackendMessage = {
  root: TreeNodeDiffModel
  leftRootName: string
  rightRootName: string
};

type SerializableFrontendState = {
  firstVisibleNode: number
  leftSelectedNode: number
  rightSelectedNode: number
  viewState: FrontendViewState
  lastBackendData?: BackendMessage
};
