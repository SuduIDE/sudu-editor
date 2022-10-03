@set target=target/wasm/test.wasm

@mkdir target\wasm

clang ^
  --target=wasm32 -nostdlib -fno-builtin -Wl,--no-entry -Wl,--export-dynamic -Wl,--import-undefined ^
  -O3 -flto  -Wl,--lto-O3  -o %target% @cppList

wasm-dis.exe %target%  > target/test.wat

@rem -Wl,--export-all
@rem -Wl,--export,__heap_base
