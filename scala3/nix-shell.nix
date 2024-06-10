{ pkgs ? import <nixpkgs> {} }:
  with pkgs.buildPackages; 
  let graal =graalvmCEPackages.graalvm-ce-musl;
  in
  pkgs.mkShell {
    nativeBuildInputs = [ scala_3 sbt graal musl.dev];

    shellHook = ''
      export GRAAL_HOME=${graal}
    '';
}
