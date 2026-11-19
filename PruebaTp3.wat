(module
  (import "env" "print_i32" (func $print_i32 (param i32)))
  (import "env" "print_f64" (func $print_f64 (param f64)))
  (import "env" "print_str" (func $print_str (param i32)))
  (import "env" "error_runtime" (func $error_runtime (param i32)))
  (memory (export "memory") 1)


  (global $a1_main__legajo_main_alumno (mut i32) (i32.const 0))
  (global $a1_main__promedio_main_alumno (mut f64) (f64.const 0.0))
  (global $a2_main__legajo_main_alumno (mut i32) (i32.const 0))
  (global $a2_main__promedio_main_alumno (mut f64) (f64.const 0.0))
  (global $a3_main__legajo_main_alumno2 (mut i32) (i32.const 0))
  (global $a3_main__promedio_main_alumno2 (mut f64) (f64.const 0.0))
  (global $a2_main (mut i32) (i32.const 0))
  (global $a3_main (mut i32) (i32.const 0))

  (func $main (export "main")
    (local $aux0 i32)
    (local $aux1 f64)
    (local $aux2 i32)
    (local $aux3 f64)
    i32.const 10
    local.set $aux0
    local.get $aux0
    global.set $a1_main__legajo_main_alumno
    f64.const 8.5
    local.set $aux1
    local.get $aux1
    global.set $a1_main__promedio_main_alumno
    global.get $a1_main__legajo_main_alumno
    global.set $a2_main__legajo_main_alumno
    global.get $a1_main__promedio_main_alumno
    global.set $a2_main__promedio_main_alumno
    global.get $a1_main__legajo_main_alumno
    global.set $a3_main__legajo_main_alumno2
    global.get $a1_main__promedio_main_alumno
    global.set $a3_main__promedio_main_alumno2
    global.get $a2_main__legajo_main_alumno
    local.set $aux2
    local.get $aux2
    call $print_i32
    global.get $a3_main__promedio_main_alumno2
    local.set $aux3
    local.get $aux3
    call $print_f64
  )

)
