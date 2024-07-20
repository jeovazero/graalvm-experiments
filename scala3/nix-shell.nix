{ pkgs ? import <nixpkgs> {} }:
  with pkgs.buildPackages; 
  let graal = if pkgs.stdenv.isLinux then graalvmCEPackages.graalvm-ce-musl else graal-ce;
  in
  pkgs.mkShell {
    nativeBuildInputs = [ scala_3 sbt graal musl.dev upx ];

    shellHook = ''
      export GRAAL_HOME=${graal}
    '';
}
