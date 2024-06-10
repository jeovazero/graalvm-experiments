{
  description = "A very basic flake";

  inputs = {
    nixpkgs.url = "github:NixOs/nixpkgs/nixos-24.05";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem(system:
        let pkgs = nixpkgs.legacyPackages.${system};
        in
        {
          devShell = import ./nix-shell.nix { inherit pkgs; };
        }
    );
  
}
