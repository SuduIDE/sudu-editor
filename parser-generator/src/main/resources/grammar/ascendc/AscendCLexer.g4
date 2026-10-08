// ============================================================================
//  Ascend C Lexer (ANTLR4)
// ----------------------------------------------------------------------------
//  Derived from:
//    "CANN Commercial 8.0.0 Ascend C Custom Operator Developer Guide"
//    Issue 01, 2025-09-09, Huawei Technologies Co., Ltd.
//
//  Ascend C is NOT a standalone language: it is C++ extended with a small set of
//  device qualifiers, a kernel-launch operator, an `AscendC` class library, and a
//  pipeline programming paradigm. This lexer therefore tokenizes:
//
//    (1) Standard C++ keywords, operators, literals, and identifiers.
//    (2) Ascend C language extensions (chapter 5 "Programming Model").
//          - function qualifiers   : __global__, __aicore__
//          - variable qualifiers   : __gm__, __ubuf__, __simd_vf__
//          - kernel-launch symbol  : <<< blockDim, l2ctrl, stream >>>
//          - GM_ADDR macro         : __gm__ uint8_t*
//    (3) Ascend C data types (chapter 16.1.3 "Definitions of Data Types").
//          - half, bfloat16_t, fixed-width integers
//          - LocalTensor / GlobalTensor / TPipe / TQue / TBuf / TPosition ...
//    (4) Ascend C macros and the pipeline API vocabulary (chapters 6, 16).
//
//  The parser grammar that consumes this lexer is expected to reference these
//  token names; keep them stable when extending.
// ============================================================================

lexer grammar AscendCLexer;

@header {
// Ascend C lexer — CANN Commercial 8.0.0 Ascend C Custom Operator Developer Guide.
// Do not edit token names casually: they are the contract for AscendCParser.
}

// ---------------------------------------------------------------------------
// Ascend C language extensions (Developer Guide §5.2 "Kernel Function")
// ---------------------------------------------------------------------------
GLOBAL            : '__global__' ; // kernel-function qualifier, callable via <<<>>>
AICORE            : '__aicore__' ; // device (AI Core) execution qualifier
GM_QUALIFIER      : '__gm__'     ; // pointer targets Global Memory (see GM_ADDR)
UBUF_QUALIFIER    : '__ubuf__'   ; // pointer/array targets Unified Buffer (Local Memory)
SIMD_VF_QUALIFIER : '__simd_vf__'; // vector-function qualifier
RESTRICT          : '__restrict' ; // standard restrict qualifier

GM_ADDR: 'GM_ADDR'; // #define GM_ADDR __gm__ uint8_t* (kernel param convention)

// Kernel-launch brackets (<<<blockDim, l2ctrl, stream>>>).  Must precede
// ShiftLeft/ShiftRight/LessThan/GreaterThan so longest-match wins cleanly.
KERNEL_LAUNCH_START : '<<<';
KERNEL_LAUNCH_END   : '>>>';

// ---------------------------------------------------------------------------
// Ascend C data types (Developer Guide §16.1.3 "Definitions of Data Types")
// ---------------------------------------------------------------------------
HALF     : 'half'      ; // 16-bit IEEE-754 half-precision (device)
BFLOAT16 : 'bfloat16_t'; // 16-bit bfloat (device)

// Core class-library / basic data structures
LOCAL_TENSOR  : 'LocalTensor'       ; // on-chip (Local Memory) tensor — §16.1.3.1
GLOBAL_TENSOR : 'GlobalTensor'      ; // Global Memory tensor          — §16.1.3.2
SHAPE_INFO    : 'ShapeInfo'         ; // shape descriptor              — §16.1.3.3
TENSOR_TRAIT  : 'TensorTrait'       ; // tensor trait                  — §16.1.3.4
UNARY_REPEAT  : 'UnaryRepeatParams' ; //                               — §16.1.3.5
BINARY_REPEAT : 'BinaryRepeatParams'; //                               — §16.1.3.6

TPIPE       : 'TPipe'      ; // resource manager (Pipe)
TQUE        : 'TQue'       ; // inter-task queue
TBUF        : 'TBuf'       ; // temporary buffer (compute only)
TPOSITION   : 'TPosition'  ; // abstract logical storage position (§16.1.2)
QUEPOSITION : 'QuePosition';

// Data-copy parameter structs
DATA_COPY_PARAMS         : 'DataCopyParams'      ;
DATA_COPY_PAD_PARAMS     : 'DataCopyPadParams'   ;
DATA_COPY_EXT_PARAMS     : 'DataCopyExtParams'   ;
DATA_COPY_PAD_EXT_PARAMS : 'DataCopyPadExtParams';

// Cube / high-level API objects
MATMUL        : 'Matmul'      ;
MATMUL_TYPE   : 'MatmulType'  ;
MATMUL_CONFIG : 'MatmulConfig';

// ---------------------------------------------------------------------------
// TPosition / QuePosition enum values (Developer Guide §16.1.2 Table 16-26)
// ---------------------------------------------------------------------------
POS_VECIN   : 'VECIN'  ; // Unified Buffer  (vector copy-in)
POS_VECOUT  : 'VECOUT' ; // Unified Buffer  (vector copy-out)
POS_VECCALC : 'VECCALC'; // Unified Buffer  (vector calc / temp)
POS_A1      : 'A1'     ; // L1 Buffer   (matmul matrix A)
POS_A2      : 'A2'     ; // L0A Buffer  (matmul matrix A tile)
POS_B1      : 'B1'     ; // L1 Buffer   (matmul matrix B)
POS_B2      : 'B2'     ; // L0B Buffer  (matmul matrix B tile)
POS_C1      : 'C1'     ; // Unified Buffer (bias matrix)
POS_C2      : 'C2'     ; // L0C Buffer  (bias tile)
POS_CO1     : 'CO1'    ; // L0C Buffer  (result tile)
POS_CO2     : 'CO2'    ; // Unified Buffer (result matrix)
POS_GM      : 'GM'     ; // Global Memory
POS_TSCM    : 'TSCM'   ; // L1 Buffer
POS_SPM     : 'SPM'    ; // L1 Buffer

// CubeFormat values used in MatmulType<TPosition::..., CubeFormat::..., T>
CUBE_ND         : 'ND'        ;
CUBE_NZ         : 'NZ'        ;
CUBE_FRACTAL_NZ : 'FRACTAL_NZ';
CUBE_ND_ALIGN   : 'ND_ALIGN'  ;

// ---------------------------------------------------------------------------
// Ascend C macros (Developer Guide §16.1.4.7, §7, §9)
// ---------------------------------------------------------------------------
REGISTER_TILING_DEFAULT       : 'REGISTER_TILING_DEFAULT'      ; // §16.1.4.7.5
REGISTER_TILING_FOR_TILINGKEY : 'REGISTER_TILING_FOR_TILINGKEY';
GET_TILING_DATA_WITH_STRUCT   : 'GET_TILING_DATA_WITH_STRUCT'  ; // §16.1.4.7.2
TILING_KEY_IS                 : 'TILING_KEY_IS'                ; // §16.1.4.7.4
REGIST_MATMUL_OBJ             : 'REGIST_MATMUL_OBJ'            ; // §16.1.5.2.1.11
IMPL_OP_OPTILING              : 'IMPL_OP_OPTILING'             ;
REGISTER_OP                   : 'REGISTER_OP'                  ;
ICPU_RUN_KF                   : 'ICPU_RUN_KF'                  ; // CPU twin-debug macro (§10)
ASCENDC_CPU_DEBUG             : 'ASCENDC_CPU_DEBUG'            ; // CPU debug guard macro

// ---------------------------------------------------------------------------
// Pipeline / memory-management / runtime API vocabulary (chapters 5, 6, 16)
// These are the paradigm's core verbs; remaining AscendC APIs (Abs, Exp,
// ReduceSum, ...) are intentionally left to IDENTIFIER to keep the lexer lean.
// ---------------------------------------------------------------------------
INIT_BUFFER       : 'InitBuffer'        ;
SET_GLOBAL_BUFFER : 'SetGlobalBuffer'   ;
SET_LOCAL_BUFFER  : 'SetLocalBuffer'    ;
ALLOC_TENSOR      : 'AllocTensor'       ;
FREE_TENSOR       : 'FreeTensor'        ;
ENQUE             : 'EnQue'             ;
DEQUE             : 'DeQue'             ;
DATA_COPY         : 'DataCopy'          ;
DATA_COPY_PAD     : 'DataCopyPad'       ;
DATA_COPY_EXT     : 'DataCopyExt'       ;
SET_SYS_WORKSPACE : 'SetSysWorkspace'   ;
GET_SYS_WORKSPACE : 'GetSysWorkSpacePtr';
GET_BLOCK_IDX     : 'GetBlockIdx'       ; // §16.1.4.4.2
GET_BLOCK_NUM     : 'GetBlockNum'       ;
GET_CORE_NUM_AIV  : 'GetCoreNumAiv'     ;
GET_CORE_NUM_AIC  : 'GetCoreNumAic'     ;
PIPE_BARRIER      : 'PipeBarrier'       ;
DEVICE_PRINTF     : 'printf'            ; // AscendC::printf on device

// ---------------------------------------------------------------------------
// Standard C++ keywords
// ---------------------------------------------------------------------------
ALIGNAS          : 'alignas'         ;
ALIGNOF          : 'alignof'         ;
ASM              : 'asm'             ;
AUTO             : 'auto'            ;
BOOL             : 'bool'            ;
BREAK            : 'break'           ;
CASE             : 'case'            ;
CATCH            : 'catch'           ;
CHAR             : 'char'            ;
CHAR8_T          : 'char8_t'         ;
CHAR16_T         : 'char16_t'        ;
CHAR32_T         : 'char32_t'        ;
CLASS            : 'class'           ;
CONST            : 'const'           ;
CONSTEXPR        : 'constexpr'       ;
CONST_CAST       : 'const_cast'      ;
CONSTEVAL        : 'consteval'       ;
CONSTINIT        : 'constinit'       ;
CONTINUE         : 'continue'        ;
CO_AWAIT         : 'co_await'        ;
CO_RETURN        : 'co_return'       ;
CO_YIELD         : 'co_yield'        ;
DECLTYPE         : 'decltype'        ;
DEFAULT          : 'default'         ;
DELETE           : 'delete'          ;
DO               : 'do'              ;
DOUBLE           : 'double'          ;
DYNAMIC_CAST     : 'dynamic_cast'    ;
ELSE             : 'else'            ;
ENUM             : 'enum'            ;
EXPLICIT         : 'explicit'        ;
EXPORT           : 'export'          ;
EXTERN           : 'extern'          ;
FALSE            : 'false'           ;
FLOAT            : 'float'           ;
FOR              : 'for'             ;
FRIEND           : 'friend'          ;
GOTO             : 'goto'            ;
IF               : 'if'              ;
INLINE           : 'inline'          ;
INT              : 'int'             ;
LONG             : 'long'            ;
MUTABLE          : 'mutable'         ;
NAMESPACE        : 'namespace'       ;
NEW              : 'new'             ;
NOEXCEPT         : 'noexcept'        ;
NULLPTR          : 'nullptr'         ;
OPERATOR         : 'operator'        ;
PRIVATE          : 'private'         ;
PROTECTED        : 'protected'       ;
PUBLIC           : 'public'          ;
REGISTER         : 'register'        ;
REINTERPRET_CAST : 'reinterpret_cast';
REQUIRES         : 'requires'        ;
RETURN           : 'return'          ;
SHORT            : 'short'           ;
SIGNED           : 'signed'          ;
SIZEOF           : 'sizeof'          ;
STATIC           : 'static'          ;
STATIC_ASSERT    : 'static_assert'   ;
STATIC_CAST      : 'static_cast'     ;
STRUCT           : 'struct'          ;
SWITCH           : 'switch'          ;
TEMPLATE         : 'template'        ;
THIS             : 'this'            ;
THREAD_LOCAL     : 'thread_local'    ;
THROW            : 'throw'           ;
TRUE             : 'true'            ;
TRY              : 'try'             ;
TYPEDEF          : 'typedef'         ;
TYPEID           : 'typeid'          ;
TYPENAME         : 'typename'        ;
UNION            : 'union'           ;
UNSIGNED         : 'unsigned'        ;
USING            : 'using'           ;
VIRTUAL          : 'virtual'         ;
VOID             : 'void'            ;
VOLATILE         : 'volatile'        ;
WCHAR_T          : 'wchar_t'         ;
WHILE            : 'while'           ;
CONCEPT          : 'concept'         ;

// Fixed-width integer type names (treated as keywords for ergonomics)
INT8_T    : 'int8_t'   ;
INT16_T   : 'int16_t'  ;
INT32_T   : 'int32_t'  ;
INT64_T   : 'int64_t'  ;
UINT8_T   : 'uint8_t'  ;
UINT16_T  : 'uint16_t' ;
UINT32_T  : 'uint32_t' ;
UINT64_T  : 'uint64_t' ;
SIZE_T    : 'size_t'   ;
SSIZE_T   : 'ssize_t'  ;
INTPTR_T  : 'intptr_t' ;
UINTPTR_T : 'uintptr_t';
PTRDIFF_T : 'ptrdiff_t';

// ---------------------------------------------------------------------------
// Literals
// ---------------------------------------------------------------------------
INTEGER_LITERAL
    : DecimalLiteral IntSuffix?
    | OctalLiteral IntSuffix?
    | HexadecimalLiteral IntSuffix?
    | BinaryLiteral IntSuffix?
    ;

FLOAT_LITERAL
    : DecimalFloat
    | HexadecimalFloat
    ;

CHAR_LITERAL
    : '\'' ( EscapeSequence | ~['\\\r\n] ) '\''
    | 'u8\'' ( EscapeSequence | ~['\\\r\n] ) '\''
    | 'u\''  ( EscapeSequence | ~['\\\r\n] ) '\''
    | 'U\''  ( EscapeSequence | ~['\\\r\n] ) '\''
    | 'L\''  ( EscapeSequence | ~['\\\r\n] ) '\''
    ;

STRING_LITERAL
    : '"' SCharSequence? '"'
    | 'u8"' SCharSequence? '"'
    | 'u"'  SCharSequence? '"'
    | 'U"'  SCharSequence? '"'
    | 'L"'  SCharSequence? '"'
    ;

RAW_STRING_LITERAL
    : 'R"' .*? '"'
    | 'u8R"' .*? '"'
    | 'uR"'  .*? '"'
    | 'UR"'  .*? '"'
    | 'LR"'  .*? '"'
    ;

// ---------------------------------------------------------------------------
// Comments & whitespace
// ---------------------------------------------------------------------------
LINE_COMMENT  : '//' ~[\r\n]*  -> channel(HIDDEN);
DOCUMENTATION : '/**' .*? '*/' -> channel(HIDDEN);
BLOCK_COMMENT : '/*' .*? '*/'  -> channel(HIDDEN);
WS            : [ \t\f]        -> channel(HIDDEN);
NEW_LINE      : NewLine        -> channel(HIDDEN);

// ---------------------------------------------------------------------------
// Preprocessor directives (kept on the default channel for tooling)
// ---------------------------------------------------------------------------
DIRECTIVE       : '#' ~[\n]*                          -> channel (HIDDEN);
MULTILINE_MACRO : '#' (~[\n]*? '\\' NewLine)+ ~ [\n]+ -> channel (HIDDEN);

// ---------------------------------------------------------------------------
// Operators & punctuation
// ---------------------------------------------------------------------------
SCOPE     : '::' ;
DOT       : '.'  ;
STAR      : '*'  ;
DOTSTAR   : '.*' ;
ARROW     : '->' ;
ARROWSTAR : '->*';
ELLIPSIS  : '...';

PLUS    : '+';
MINUS   : '-';
SLASH   : '/';
PERCENT : '%';
CARET   : '^';
AMP     : '&';
PIPE    : '|';
TILDE   : '~';
BANG    : '!';
ASSIGN  : '=';

PLUS_ASSIGN    : '+=' ;
MINUS_ASSIGN   : '-=' ;
STAR_ASSIGN    : '*=' ;
SLASH_ASSIGN   : '/=' ;
PERCENT_ASSIGN : '%=' ;
CARET_ASSIGN   : '^=' ;
AMP_ASSIGN     : '&=' ;
PIPE_ASSIGN    : '|=' ;
SHL_ASSIGN     : '<<=';
SHR_ASSIGN     : '>>=';

EQUAL         : '==' ;
NOT_EQUAL     : '!=' ;
LESS          : '<'  ;
GREATER       : '>'  ;
LESS_EQUAL    : '<=' ;
GREATER_EQUAL : '>=' ;
SPACESHIP     : '<=>';

LOGICAL_AND : '&&';
LOGICAL_OR  : '||';
SHIFT_LEFT  : '<<';
SHIFT_RIGHT : '>>';

INCREMENT : '++';
DECREMENT : '--';

LPAREN : '(';
RPAREN : ')';
LBRACK : '[';
RBRACK : ']';
LBRACE : '{';
RBRACE : '}';

QUESTION : '?';
COLON    : ':';
SEMI     : ';';
COMMA    : ',';

// ---------------------------------------------------------------------------
// Identifier (must come AFTER all keyword rules)
// ---------------------------------------------------------------------------
IDENTIFIER
    : [a-zA-Z_] [a-zA-Z0-9_]*
    ;

// ---------------------------------------------------------------------------
// Unknown character
// ---------------------------------------------------------------------------
UNKNOWN: .;

// ---------------------------------------------------------------------------
// Fragments
// ---------------------------------------------------------------------------
fragment NewLine            : ('\r'? '\n' | '\r');
fragment DecimalLiteral     : [1-9] [0-9]*;
fragment OctalLiteral       : '0' [0-7]*;
fragment HexadecimalLiteral : '0' [xX] [0-9a-fA-F]+;
fragment BinaryLiteral      : '0' [bB] [01]+;
fragment IntSuffix          : [uU] [lL]? | [lL] [uU]?;

fragment DecimalFloat
    : Digits '.' Digits? ExponentPart? FloatSuffix?
    | '.' Digits ExponentPart? FloatSuffix?
    | Digits ExponentPart FloatSuffix?
    ;

fragment HexadecimalFloat
    : '0' [xX] HexDigits '.' HexDigits? BinaryExponentPart FloatSuffix?
    | '0' [xX] '.' HexDigits BinaryExponentPart FloatSuffix?
    | '0' [xX] HexDigits BinaryExponentPart FloatSuffix?
    ;

fragment Digits            : [0-9]+;
fragment HexDigits         : [0-9a-fA-F]+;
fragment ExponentPart      : [eE] [+-]? Digits;
fragment BinaryExponentPart: [pP] [+-]? Digits;
fragment FloatSuffix       : [fFlL];

fragment EscapeSequence
    : '\\' [abfnrtv'"\\?]
    | '\\' 'x' [0-9a-fA-F]+
    | '\\' 'u' [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F]
    | '\\' 'U' [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F]
                 [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F]
    | '\\' [0-7] [0-7]? [0-7]?
    ;

fragment SCharSequence : SChar+;
fragment SChar
    : ~["\\\r\n]
    | EscapeSequence
    ;
