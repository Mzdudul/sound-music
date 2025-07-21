package com.mzz.sound_music.principal;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.mzz.sound_music.model.Artista;
import com.mzz.sound_music.model.Musica;
import com.mzz.sound_music.model.TipoArtista;
import com.mzz.sound_music.repository.ArtistaRepository;


public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private final ArtistaRepository repositorio;

    public Principal(ArtistaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;

        while (opcao != 9) {
            var menu = """
                    1 - Cadastrar artista
                    2 - Cadastrar música
                    3 - Listar músicas
                    4 - Busxar músicas por artista
                   

                    9 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtista();
                    break;
                case 2:
                    cadastrarMusica();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    buscarMusicasPorArtista();
                    break;
                
                case 9:
                    System.out.println("Saindo...");
                    break;
                default:
                    break;
            }

        }
    }

    

    private void buscarMusicasPorArtista() {
        System.out.println("Buscar musicas de que artista?");
        var nome = leitura.nextLine();
        List<Musica> musicas = repositorio.buscaMusicasPorArtista(nome);
        musicas.forEach(System.out::println);

    }

    private void listarMusicas() {
        List<Artista> artistas = repositorio.findAll();
        artistas.forEach(a -> a.getMusicas().forEach(System.out::println));
    }

    private void cadastrarMusica() {
        System.out.println("Cadastrar música de qual artista?");
        var nomeArtista = leitura.nextLine();
        Optional<Artista> artista = repositorio.findByNomeContainingIgnoreCase(nomeArtista);
        if (artista.isPresent()) {
            System.out.println("Informe o título da música:");
            var nomeMusica = leitura.nextLine();
            Musica musica = new Musica(nomeMusica);
            musica.setTitulo(nomeMusica);
            musica.setArtista(artista.get());
            artista.get().getMusicas().add(musica);
            repositorio.save(artista.get());
        } else {
            System.out.println("Artista nao encontrado");
        }
    }

    private void cadastrarArtista() {
        var cadastrarNovoArtista = "S";

        while (cadastrarNovoArtista.equalsIgnoreCase("S")) {

            System.out.println("Informe o nome do artista:");
            var nome = leitura.nextLine();
            System.out.println("Informe o tipo do artista (BANDA, SOLO ou DUPLA):");
            var tipo = leitura.nextLine();
            TipoArtista tipoArtista = TipoArtista.valueOf(tipo.toUpperCase());
            Artista artista = new Artista(nome, tipoArtista);
            repositorio.save(artista);
            System.out.println("Cadastrar novo artista? (S/N)");
            cadastrarNovoArtista = leitura.nextLine();
        }
    }
}
