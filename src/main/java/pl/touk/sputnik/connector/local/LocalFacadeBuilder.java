package pl.touk.sputnik.connector.local;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.IOException;

public class LocalFacadeBuilder {
    public LocalFacade build() {
        try (Repository repository = new FileRepositoryBuilder().readEnvironment().findGitDir().build()) {
            try (Git git = new Git(repository)) {
                return new LocalFacade(git, new LocalFacadeOutput());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error getting git repository", e);
        }
    }
}
