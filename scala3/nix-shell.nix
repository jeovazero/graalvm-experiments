{ pkgs ? import <nixpkgs> {} }:
  with pkgs.buildPackages; 
  let graal = graalvmCEPackages.graalvm-ce-musl;
  in
  pkgs.mkShell {
    nativeBuildInputs = [ scala_3 sbt graal musl.dev upx ];

    shellHook = ''
      export GRAAL_HOME=${graal}
      echo 'GRAAL_HOME=${graal}' > .temp_env
    '';
}
