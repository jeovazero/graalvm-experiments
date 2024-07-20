{ pkgs ? import <nixpkgs> {} }:
  with pkgs.buildPackages; 
  let muslOrNotPkg = if pkgs.stdenv.isLinux then [ musl.dev  ] else [ ];
      graal = if pkgs.stdenv.isLinux then graalvmCEPackages.graalvm-ce-musl else graalvm-ce;
  in
  pkgs.mkShell {
    nativeBuildInputs = [ scala_3 sbt upx graal ] ++ muslOrNotPkg;
    shellHook = ''
      export GRAAL_HOME=${graal}
      # https://github.com/oracle/graal/pull/6095
      # https://github.com/oracle/graal/issues/7502
      export NATIVE_IMAGE_DEPRECATED_BUILDER_SANITATION="true"
    '';
}
